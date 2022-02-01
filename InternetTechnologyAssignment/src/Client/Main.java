package Client;

import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Main {
    public static void main(String[] args) {
        Client client = new Client(Integer.parseInt(args[1]), args[0]);
        client.eshtablishSocketConnection();
//        String getPatternString = "(get) (\\S+)";
//        String putPatternString = "(put) (\\S+) (\\S+)";


        String text = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
//        System.out.println(text);
        String loginString = "(-login) (\\S+) (\\S+)";
        Pattern loginPattern = Pattern.compile(loginString);
        Matcher loginMatcher = loginPattern.matcher(text);

        String checkingPatternString = "((((get )(\\S+))|((put )(\\S+) (\\S+))|(getall))(\\s|$))*";
        Pattern checkingPattern = Pattern.compile(checkingPatternString);
        Matcher checkingMatcher = checkingPattern.matcher(text);

        boolean access = false;

        if(loginMatcher.find()){
            text = String.join(" ", Arrays.copyOfRange(args, 5, args.length));
            checkingMatcher = checkingPattern.matcher(text);

            access = client.login(loginMatcher.group(2), loginMatcher.group(3));

            if(access)
            {
                System.out.println("operating as manager.....");
                if(checkingMatcher.matches())
                {
                    String patternString = "((get) (\\S+))|((put) (\\S+) (\\S+))|(getall)";

                    Pattern pattern = Pattern.compile(patternString);

                    Matcher matcher = pattern.matcher(text);

                    while(matcher.find()){
                        String tokens[] = matcher.group().split(" ");
                        if(tokens[0].equals("put")){
                            client.put(tokens[1], tokens[2]);
                        }
                        else if (tokens[0].equals("get")){
                            System.out.println(client.get(tokens[1]));
                        }
                        else
                        {
                            client.getAll();
                        }
                    }
                }
                else{
                    System.out.println("command line instructions are wrong !!");
                }
            }
            else{
                System.out.println("incorrect user name or password");
            }
        }
        else{
            //work as guest
            System.out.println("operating as guest.....");
            if(checkingMatcher.matches())
            {
                String patternString = "((get) (\\S+))|((put) (\\S+) (\\S+))";

                Pattern pattern = Pattern.compile(patternString);

                Matcher matcher = pattern.matcher(text);

                while(matcher.find()){
                    String tokens[] = matcher.group().split(" ");
                    if(tokens[0].equals("put")){
                        client.put(tokens[1], tokens[2]);
                    }
                    else if (tokens[0].equals("get")){
                        System.out.println(client.get(tokens[1]));
                    }
                }
            }
            else{
                System.out.println("command line instructions are wrong !!");
            }
        }

        Scanner sc = new Scanner(System.in);
        int s;
        do{
            System.out.println("-----======================-----");
            System.out.println("case 1 : get with key");
            System.out.println("case 2 : put with key and value");
            System.out.println("case 3 : get All data ( manager only ) ");
            System.out.println("case 4 : exit !");

            s = sc.nextInt();
            sc.nextLine();

            switch (s){
                case 1: {
                    String key = sc.nextLine();
                    System.out.println(client.get(key));
                    break;
                }

                case 2 : {
                    String key, val;
                    key = sc.nextLine();
                    val = sc.nextLine();
                    client.put(key,val);
                    break;
                }

                case 3 : {
                    if(access){
                        client.getAll();
                    }
                }


                default:
                    break;
            }
        }
        while (s!=4);
        client.endSocketConnection();
    }
}
