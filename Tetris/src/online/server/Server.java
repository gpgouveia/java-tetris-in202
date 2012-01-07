

package online.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import online.util.ConnectionCodes.PlayerQueryCodes;
import online.util.ConnectionCodes.ServerQueryCodes;
import online.util.Match;
import online.util.Player;
/**
 *
 * @author gustavo
 */
public class Server extends Thread {

    static int playerCount = 0;
    static int matchCount = 0;
    static HashMap<Integer,Player> playerMap;
    static HashMap<Integer,Match> matchMap;


    public static void main(String[] args) {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(4779);
            System.out.println("host ");
        } catch (java.net.BindException ex) {
            ex.printStackTrace();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        playerMap = new HashMap<Integer, Player>();
        matchMap  = new HashMap<Integer,  Match>();

        if (ss == null) {
            System.out.println("Error, could not bind to port 4779.");
        } else {
            while (true) {
                try {
                    /*
                     * Waits until a connection is required.
                     */
                    Socket connection = ss.accept();
                    Server server = new Server(connection);
                    server.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private Socket clientConnection;
    private Player player;
    OutputStreamWriter os = null;
    BufferedReader br = null;


    public Server(Socket clientConnection) {
        this.clientConnection = clientConnection;
    }

    @Override
    public void run() {
        try {
            br = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));
            os = new OutputStreamWriter(clientConnection.getOutputStream());
            
            int timeout = 0;
            boolean barN = false;


            while (timeout < 2000) {
                try {
                    sleep(100);
                } catch (InterruptedException e1) {}
                timeout++;
                if(clientConnection.isClosed())
                    break;
                if(!clientConnection.isConnected())
                    break;
                if (br.ready()) {
                    String q = br.readLine();
                    q = q.trim();
                    /**
                     * Detects two lines and terminates connection if they appear
                     */
                    if(q.length() == 0){ // connection terminated;
                        if(barN){
                            break;
                        }
                        barN = true;
                    }else{
                        barN = false;
                        timeout = 0;
                        System.out.println("R  "+q);
                        consume(q);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            cleanClient();
            try {
                br.close();
            } catch (Exception e) {}
            try {
                os.close();
            } catch (Exception e) {}
        }
    }



    private void cleanClient() {
        try {
            send("\n\n");
        } catch (Exception e) {}

        try{
            Match m = player.getMatch();
            Player other = m.getOtherPlayer(player.getPlayerId());

            other.getServer().send("\n\n");
        } catch (Exception e) {}

        try{
            Match m = player.getMatch();
            matchMap.remove(m.getMatchid());
        } catch (Exception e) {}

        try{
            playerMap.remove(player.getPlayerId());
        } catch (Exception e) {} 
    }
    static private void cleanMatch(Match m){
        try{
            matchMap.remove(m.getMatchid());
            for(int i:m.getPlayersIds()){
                        throw new UnsupportedOperationException("Not yet implemented");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void consume(String q) throws IOException {
        String[] query = q.split("\\s");
        if(query.length < 1)
            return;
        PlayerQueryCodes code = null;
        try{
            code = PlayerQueryCodes.valueOf(query[0].toUpperCase());
        }catch(Exception e){
            e.printStackTrace();
        }
        
        switch(code){
            case CREATECLIENT:
                createClient(this,query[1]);
                break;
            case PLIST:
                sendPlayerList();
                break;
            case MATCHREQ:
                requestMatchWith(query[1]);
                break;
            case MATCHACCEPT:
                acceptMatch(query[1]);
                break;
            case GAMEPOINT:
                sendGamePoint();
                break;
            case GAMEOVER:
                sendGameOver();
                break;
            case MATCHDECLINE:
                sendMatchDeclined(query[1]);
                break;
            default:
                break;
        }

    }


    private void send(String str) throws IOException{
        os.write(str+"\n");
        os.flush();
    }

    private void sendPlayerList() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(ServerQueryCodes.PLIST.toString());
        sb.append(" ");
        for(Entry<Integer,Player> entry: playerMap.entrySet()){
            sb.append(entry.getKey());
            sb.append(">");
            sb.append(entry.getValue().getName());
            sb.append(">");
            sb.append(entry.getValue().getState());
            sb.append("#");
        }
        send(sb.toString());
    }

    synchronized static private void createClient(Server s, String name) {
        int pid = playerCount++;
        
        s.player = new Player(name,s,pid);
        playerMap.put(pid, s.player);
    }
    
    synchronized static private void createMatch(Player p1, Player p2){
        int mid = matchCount++;
        
        Match m = new Match(p1, p1.getServer(), p2, p2.getServer(), mid);
        matchMap.put(mid, m);
        try {
            p1.getServer().send(ServerQueryCodes.MATCHSTART + " " + mid);
            p2.getServer().send(ServerQueryCodes.MATCHSTART + " " + mid);
            p1.setState(Player.PlayerState.PLAYING);
            p2.setState(Player.PlayerState.PLAYING);
        } catch (IOException ex) {
            cleanMatch(m);
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void requestMatchWith(String idStr) throws IOException {
        Integer id = -1;
        try{
            id = Integer.valueOf(idStr);
            Player opp = playerMap.get(id);
            if(opp.getState() == Player.PlayerState.ONLINE){
                opp.getServer().requestFrom(this.player);
            } else {
                id = -1;
            }
        } catch (NumberFormatException e){}

        if(id == -1){
            send(ServerQueryCodes.ERROR+" "+PlayerQueryCodes.MATCHREQ+"#"+idStr);
        }
    }

    private void acceptMatch(String idStr) throws IOException {
        Integer id = -1;
        try{
            id = Integer.valueOf(idStr);
            Player opp = playerMap.get(id);
            if(opp.getState() == Player.PlayerState.ONLINE){
                createMatch(this.player,opp);
            } else {
                id = -1;
            }
        } catch (NumberFormatException e){
        }finally{
            if(id == -1){
                send(ServerQueryCodes.ERROR+" "+PlayerQueryCodes.MATCHACCEPT+"#"+idStr);
            }
        }
    }

    private void requestFrom(Player player) throws IOException {
        send(ServerQueryCodes.MATCHREQ+" "+player.getPlayerId());
    }

    private void sendGamePoint() throws IOException {
        Match m = player.getMatch();
        int playerid = player.getPlayerId();
        int matchid = m.getMatchid();
        m.getOtherServer(playerid).send(ServerQueryCodes.GAMEPUNN+" "+matchid);
    }

    private void sendGameOver() throws IOException {
        Match m = player.getMatch();
        m.getPlayerWithid(m.getPlayersIds()[0]).setState(Player.PlayerState.ONLINE);
        m.getPlayerWithid(m.getPlayersIds()[1]).setState(Player.PlayerState.ONLINE);

        m.getOtherServer(player.getPlayerId()).send(ServerQueryCodes.ENDGAME+" "+m.getMatchid());
    }

    private void sendMatchDeclined(String idStr) throws IOException {
        Integer id = -1;
        try{
            id = Integer.valueOf(idStr);
            Player opp = playerMap.get(id);
            if(opp.getState() == Player.PlayerState.ONLINE){
                opp.getServer().send(ServerQueryCodes.MATCHDECLINED+" "+player.getPlayerId());
            } else {
                id = -1;
            }
        } catch (NumberFormatException e){
        }finally{
            if(id == -1){
                send(ServerQueryCodes.ERROR+" "+ServerQueryCodes.MATCHDECLINED+"#"+player.getPlayerId());
            }
        }
    }
}

