package client_simple;
public class Client_Simple {
    public final static int PORT = 1337;
    public static void main(String[] args){
        try{
            Client client = new Client();
            client.chooseSession();


        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
