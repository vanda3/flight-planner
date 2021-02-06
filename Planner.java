import java.util.*;
import java.io.*;

class Timetable{
    String origin;
    String destination;
    ArrayList<Flight> info;
    Timetable(String origin, String destination, ArrayList<Flight> info){
        this.info=info;
        this.origin=origin;
        this.destination=destination;
    }
}

class Flight{
    int arrival[]=new int[2];
    int departure[]=new int[2];
    String number;
    String days[];
    Flight(String days, String number, String departure, String arrival){
        int temp_dep, temp_arr;
        int i=0, j=0;
        temp_dep=Integer.parseInt(departure);
        temp_arr=Integer.parseInt(arrival);
        this.arrival[0]=temp_arr/100;
        this.arrival[1]=temp_arr-this.arrival[0]*100;
        this.departure[0]=temp_dep/100;
        this.departure[1]=temp_dep-this.departure[0]*100;
        this.days= new String[7];
        this.number=number;
        
        if(days.contains("alldays")){
            this.days[0] = "mo";
            this.days[1] = "tu";
            this.days[2] = "we";
            this.days[3] = "th";
            this.days[4] = "fr";
            this.days[5] = "sa";
            this.days[6] = "su";
        }
        else{
            for(i=0; i<7; i++){
                if(i==0 && days.contains("mo"))
                    this.days[0] = "mo";
                else if(i==1 && days.contains("tu"))
                    this.days[1] = "tu";
                else if(i==2 && days.contains("we"))
                    this.days[2] = "we";
                else if(i==3 && days.contains("th"))
                    this.days[3] = "th";
                else if(i==4 && days.contains("fr"))
                    this.days[4] = "fr";
                else if(i==5 && days.contains("sa"))
                    this.days[5] = "sa";
                else if(i==6 && days.contains("su"))
                    this.days[6] = "su";
                else
                    this.days[i]= "";
            }
        }
    }
    
}

class Planner{
    public static String data = "data.txt";
    // Contém todas as ligações
    public static ArrayList<Timetable> routes= new ArrayList<Timetable>();
    public static Graph map=new Graph();
    public static String s="", ori="", dest="", dest2="", dest3="";
    public static int dep_day=0, arr_day=0, inter, i=0, rdtrip, specific_date=1, duration;
    public static void main(String args[]){
        Scanner scan= new Scanner(System.in);
        boolean contains=false;
        
        parse_data();
        System.out.println();
        
        map_cities(routes);

        System.out.println("******** MENU ********");
        System.out.println();
        
        // IDA E VOLTA
        System.out.println("Escolha uma das opções:");
        System.out.println("1) Viagem de ida");
        System.out.println("2) Viagem de e volta com 3 destinos");
        rdtrip=scan.nextInt();
        System.out.println();
        while(rdtrip!=1 && rdtrip!=2){
            System.out.println("Resposta inválida!");
            System.out.println();
            System.out.println("Escolha uma das opções:");
            System.out.println("1) Viagem de ida");
            System.out.println("2) Viagem de e volta com 3 destinos");
            rdtrip=scan.nextInt();
            System.out.println();
        }
        
        // INTERMÉDIOS
        if(rdtrip==1){
            System.out.println("Escolha uma das opções:");
            System.out.println("1) Voo directo");
            System.out.println("2) Voo com transfers");
            inter=scan.nextInt();
            System.out.println();
        }
        if(rdtrip==2) // um voo de ida e volta só considera com vários destinos
            inter=3;
        while(inter!=1 && inter!=2 && rdtrip==1){
            System.out.println("Resposta inválida!");
            System.out.println();
            System.out.println("Escolha uma das opções:");
            System.out.println("1) Voo directo");
            System.out.println("2) Voo directo ou com transfers");
            inter=scan.nextInt();
            System.out.println();
        }
        
        // CIDADE ORIGEM
        System.out.println("Cidade de origem: ");
        scan.nextLine();
        ori=scan.nextLine();
        System.out.println();
        if(!map.cityExists(ori)){
            System.out.println("Cidade indisponível!");
            System.out.println();
            System.out.println("Cidade de origem: ");
            ori=scan.nextLine();
            System.out.println();
        }
        contains=false;
        
        // CIDADE DE DESTINO (IDA)
        if(rdtrip==1){
            System.out.println("Cidade de destino: ");
            dest=scan.nextLine();
            System.out.println();
            while(!map.cityExists(dest)){
                System.out.println("Destino indisponível!");
                System.out.println();
                System.out.println("Cidade de destino: ");
                dest=scan.nextLine();
                System.out.println();
            }
        }
        else{ // CIDADES DE DESTINO (IDA e VOLTA)
            System.out.println("Cidade de destino 1: ");
            dest=scan.nextLine();
            System.out.println();
            while(!map.cityExists(dest)){
                System.out.println("Destino indisponível!");
                System.out.println();
                System.out.println("Cidade de destino 1: ");
                dest=scan.nextLine();
                System.out.println();
            }
            System.out.println("Cidade de destino 2: ");
            dest2=scan.nextLine();
            System.out.println();
            while(!map.cityExists(dest2)){
                System.out.println("Destino indisponível!");
                System.out.println();
                System.out.println("Cidade de destino 2: ");
                dest2=scan.nextLine();
                System.out.println();
            }
            System.out.println("Cidade de destino 3: ");
            dest3=scan.nextLine();
            System.out.println();
            while(!map.cityExists(dest3)){
                System.out.println("Destino indisponível!");
                System.out.println();
                System.out.println("Cidade de destino 3: ");
                dest3=scan.nextLine();
                System.out.println();
            }
        }
        
        // DATA ESPECIFICA
        if(rdtrip==1 && inter==1){
            System.out.println("Escolha uma das opções:");
            System.out.println("1) Voos directos disponíveis para um dado dia");
            System.out.println("2) Dias em que há voos directos");
            specific_date=scan.nextInt();
            System.out.println();
            while(specific_date!=1 && specific_date!=2){
                System.out.println("Resposta inválida!");
                System.out.println();
                System.out.println("Pretende viajar num dia em específico?");
                System.out.println("1) Sim");
                System.out.println("2) Não");
                specific_date=scan.nextInt();
                System.out.println();
            }
        }
        else{ // Se ida e volta, viaja sempre num dia especifico
            specific_date=1;
        }
        
        // PARTIDA DIA ESPECIFICO
        if(specific_date==1){
            System.out.println("***** Partida");
            System.out.println("Dia da semana (1-7 onde 1 é segunda): ");
            dep_day=scan.nextInt();
            System.out.println();
            while(dep_day<1 || dep_day>7){
                System.out.println("Resposta inválida!");
                System.out.println();
                System.out.println("***** Partida");
                System.out.println("Dia da semana (1-7 onde 1 é segunda): ");
                dep_day=scan.nextInt();
                System.out.println();
            }
        }
        
        // CHEGADA
        if(rdtrip==2){
            System.out.println("***** Chegada");
            System.out.println("Dia da semana (1-7 onde 1 é segunda): ");
            arr_day=scan.nextInt();
            if(arr_day>dep_day)
                duration=arr_day-dep_day;
            else
                duration=7-dep_day+arr_day;
            System.out.println();
            while(arr_day<1 || arr_day>7 || duration<3){
                if(arr_day>dep_day)
                    duration=arr_day-dep_day;
                else
                    duration=7-dep_day+arr_day;
                System.out.println();
                if(duration<3)
                     System.out.println("A duração total da viagem tem de ser de pelo menos 3 dias.");
                else
                     System.out.println("Resposta inválida!");
                System.out.println();
                System.out.println("***** Partida");
                System.out.println("Dia da semana (1-7 onde 1 é segunda): ");
                arr_day=scan.nextInt();
                System.out.println();
            }
        }
        
        // Ida e volta
        if(rdtrip==2)
            roundtrip(ori, dest, dest2, dest3);
        // Ida
        else
            oneway(ori, dest, dep_day);   
    }
    
    /******************************** IDA E VOLTA **********************************/
    // In which sequence I should visit cities C1, C2 and C3 so that I don’t need to have more than one flight a day?
    static void roundtrip(String ori, String dest, String dest2, String dest3){
        String S=ori, C1="", C2="", C3="";
        boolean conn1=false, conn2=false, conn3=false;
        List<String> cits = new ArrayList<String>();
        List<String> sol = new ArrayList<String>();
        cits.add(dest);
        cits.add(dest2);
        cits.add(dest3);
        
        int ci[]={0,1,2}, i, j, k;
        List<List<Integer>> perms = permute(ci);
        List<Integer> tmp;

        for(k=0; k<perms.size(); k++){
            tmp=perms.get(k);
            C1= cits.get(tmp.get(0));
            C2= cits.get(tmp.get(1));
            C3= cits.get(tmp.get(2));
        
            if(directFlight(S,C1,dep_day,false) && directFlight(C3,S,arr_day,false)){
                if(dep_day<arr_day){
                    for(i=dep_day+1; i<arr_day; i++){
                        for(j=i+1; j<arr_day ;j++){
                            if(directFlight(C1,C2,i,false) && directFlight(C2,C3,j,false) && sol.isEmpty()){
                                sol.add(S);
                                sol.add(C1);
                                sol.add(C2);
                                sol.add(C3);
                            }
                        }
                    }
                }
                else{
                    duration=7-dep_day+arr_day;
                    for(i=1; i<7 && (i>dep_day || i<arr_day) ; i++){
                        for(j=1; j<7 && (j>dep_day || j<arr_day); j++){
                            if(((i<j && j<arr_day && i<arr_day) || (i<j && j>dep_day && i>dep_day) || (i<j && i>dep_day && j<arr_day)) && directFlight(C1,C2,i,false) && directFlight(C2,C3,j,false) && sol.isEmpty()){
                                sol.add(S);
                                sol.add(C1);
                                sol.add(C2);
                                sol.add(C3);
                            } 
                        }
                    }

                }
            }
            
        }
        if(sol.isEmpty())
            System.out.print("Não há solução!\n");
        else{ 
            System.out.println("A sequência de voos é:");
            for(String st:sol){
                System.out.print(st+" ");
            }
            System.out.print(S);
        }
        System.out.println();
        System.out.println();
    }
    
    /****************************  UTILS ROUNDTRIP  ****************************/
    // Permutation
    static List<List<Integer>> permute(int[] ci) {
        List<List<Integer>> list = new ArrayList<List<Integer>>();
        int size = 6;
        List<Integer> seq = new ArrayList<Integer>();
        for(int a:ci)
            seq.add(a);
        list.add(seq);
        for(int i = 0;i < size - 1;i++){
            seq = new ArrayList<Integer>();
            nextPermutation(ci);
            for(int a:ci){
                seq.add(a);
            }
            list.add(seq);
        }
        return list;
    }
    
    static void nextPermutation(int[] ci){
        int i = 2;
        while(i > 0 && ci[i-1] >= ci[i])
            i--;
        if(i==0)
            reverse(ci,0,ci.length-1);
        else{
            int j = i;
            while(j < ci.length && ci[j] > ci[i-1])
                j++;
            int tmp = ci[i-1];
            ci[i-1] = ci[j-1];
            ci[j-1] = tmp;
            reverse(ci,i,ci.length-1);  
        }
    }
    
    static void reverse(int[] arr,int start, int end){
        int tmp;
        for(int i = 0; i <= (end - start)/2; i++ ){
            tmp = arr[start + i];
            arr[start + i] = arr[end - i];
            arr[end - i ] = tmp;
        }
    }

    /********************************** IDA **********************************/
    // IDA
    static void oneway(String ori, String dest, int day){ // ori dest
        
        // VOOS DIRECTOS DE A PARA B NO DIA X
        if(specific_date==1 && inter==1){
            directFlight(ori,dest,dep_day,true);
            System.out.println(s);
        }
        
        
        // VOOS DIRECTOS E/OU COM TRANSFERS DE A PARA B NO DIA X
        else if(specific_date==1 && inter==2){
            boolean not_dir=false;
            // DIRECTOS
            directFlight(ori,dest,dep_day,true);
            // COM TRANSFERS
            LinkedList<String> visited = new LinkedList<String>();
            visited.add(ori);
            not_dir=depthFirst(visited,ori,dest,day);
            System.out.println(s);
            if(not_dir){
                System.out.println("Não há voos com transfers!\n\n");
            }
        }
        
        
        // DIAS EM QUE É POSSÍVEL VIAJAR DE A PARA B - DIRECTO
        else if(specific_date==2 && inter==1){
            if(map.routeExists(ori,dest)){
                System.out.println("Dias com voos directos de "+ori+" para "+dest+":");
                Timetable c = map.getTimetable(ori,dest);
                checkDate(c);
                System.out.println();
            }
            else{
                System.out.println("Não existem voos directos!\n");
                System.out.println();
            }
            
        }  
    }

    /********************************* UTILS IDA ********************************/
    //  VOOS DIRECTOS
    static boolean directFlight(String ori, String dest, int dep_day, boolean print){
        String k="", tmp="";
        boolean exists=false;
        if(map.routeExists(ori,dest)){
            k+=("**********  "+ori+" : "+dest+"  **********\n\n");
            Timetable c = map.getTimetable(ori,dest);
            tmp=checkTime(c,dep_day);
            if(tmp.equals(""))
                k="Não existem voos directos!\n\n";
            else{
                k+=tmp;
                k+="\n";
                exists=true;
            }
        }
        else
            k="Não existem voos directos!\n\n";
        if(print)
            s+=k;
        return exists;
    }
    
    // VOOS DE A PARA B NO DIA X
    static String checkTime(Timetable c, int day){
        String k="";
        for(Flight d: c.info){
            if(!d.days[day-1].equals("")){
                k+=(ori+" -> "+dest+" | "+d.departure[0]+"."+d.departure[1]+" | "+d.arrival[0]+"."+d.arrival[1]+" | "+d.number+"\n");
            }
        }
        return k;
    }
    
    // DIAS EM QUE SE PODE VIAJAR ENTRE A E B - DIRECTOS
    static void checkDate(Timetable c){
        String temp_days[]=new String[7];
        int i;
        for(i=0; i<7; i++){
            temp_days[i]="";
        }
        for(Flight d: c.info){
            for(i=0; i<7; i++){
                if(!d.days[i].equals("")){
                    temp_days[i]=d.days[i];
                }
            }
        }
        for(i=0; i<7; i++){
            if(!temp_days[i].equals(""))
                System.out.print(temp_days[i]+" ");
        }
        System.out.println();
    }
    
    // VERIFICA SE TRANSFERS SÃO POSSIVEIS 
    static boolean canTransfer(String a, String b, String c, int day, int last_arr){
        int arr, dep, dep2;
        Timetable r1 = map.getTimetable(a,b);
        Timetable r2 = map.getTimetable(b,c);
        for(Flight af: r1.info){
            for(Flight bf: r2.info){
                if(!af.days[day-1].equals("") && !bf.days[day-1].equals("")){
                    if(a.equals(ori))
                            last_arr=0;
                    arr=af.arrival[0]*60+af.arrival[1];
                    dep=af.departure[0]*60+af.departure[1];
                    dep2=bf.departure[0]*60+bf.departure[1];
                    if((dep2-arr)>=40 && (dep-last_arr)>=40)
                        return true;
                }
            }
        }
        return false;
    }
    
    // DFS - CAMINHOS INDIRECTOS POSSÍVEIS ENTRE A E B
    static boolean depthFirst(LinkedList<String> visited, String ori, String dest, int day){
        LinkedList<String> cities = map.next(visited.getLast());
        boolean exists=false;
        // Cidades adjacentes
        for(String c : cities){
            if(visited.contains(c)){
                continue;
            }
            if(c.equals(dest)){
                visited.add(c);
                exists=path(visited,ori,dest,day,false);
                if(exists){
                    s+="\n";
                    path(visited,ori,dest,day,true);
                }
                visited.removeLast();
                break;
            }
        }
        for(String c : cities){
            if(visited.contains(c) || c.equals(dest)){
                continue;
            }
            visited.addLast(c);
            depthFirst(visited,ori,dest,day);
            visited.removeLast();
        }
        return exists;
    }
    
    // IMPRIME CADA SOLUÇÃO DO DFS
    static boolean path(LinkedList<String> visited, String ori, String dest, int day,boolean print){
        String tmp="", tmp1="", tmp2="", header="";
        String prev, act, a, b, c;
        int i, last_arr=0, dep, arr, dep2, j;
        boolean exists=false;
        Timetable r1, r2;
        
        prev=visited.get(0); // cidade a
        act=visited.get(1); // cidade b - transfer
        for(i=2; i< visited.size(); i++){
            a=prev; //cidade a
            b=act;  //cidade b- transfer
            c=visited.get(i); // cidade c
            if(a.equals(ori) && c.equals(dest))
                header=("******** "+a+" : "+b+" : "+c+" ********\n\n");
            else if(a.equals(ori))
                header=("******** "+a+" : "+b+" : "+c+" : ");
            else if(c.equals(dest))
                header+=(c+" ********\n\n");
            else
                header+=(c+" : ");
            r1=map.getTimetable(a,b); 
            r2=map.getTimetable(b,c); 
            for(Flight f1:r1.info){ 
                for(Flight f2:r2.info){ 
                    if(canTransfer(a,b,c,day,last_arr) && !f1.days[day-1].equals("") && !f2.days[day-1].equals("")){ // verifica se existem voos no dia indicado em ambas as rotas
                        if(a.equals(ori)) // se a é origem, a ultima chegada nao existe, ou seja, é 0
                            last_arr=0;
                        dep=f1.departure[0]*60+f1.departure[1];
                        arr=f1.arrival[0]*60+f1.arrival[1];
                        dep2=f2.departure[0]*60+f2.departure[1];
                        if((dep2-arr)>=40 && (dep-last_arr)>=40){
                            tmp+=(a+" -> "+b+" | ");
                            tmp+=(f1.departure[0]+"."+f1.departure[1]+" | "+f1.arrival[0]+"."+f1.arrival[1]+" | "+f1.number+"\n"); 
                            if(c.equals(dest)){
                                tmp+=(b+" -> "+c+" | ");
                                tmp+=(f2.departure[0]+"."+f2.departure[1]+" | "+f2.arrival[0]+"."+f2.arrival[1]+" | "+f2.number+"\n\n");
                                if(!tmp1.equals("") && tmp1.contains(ori+" -> ")){
                                    tmp2+=tmp1;
                                    tmp2+=tmp;
                                    tmp1="";
                                }
                                else if(tmp1.equals("") && tmp.contains(ori+" -> ")){
                                    tmp2+=tmp;
                                }
                                tmp="";
                            }
                            else{
                                tmp1+=tmp;
                                tmp="";
                                last_arr=f1.arrival[0]*60+f1.arrival[1];
                            }
                        }
                    }
                }
            }
            if(!tmp2.contains(ori+" -> ") && tmp2.contains(" -> "+dest)){
                tmp1="";
                tmp2="";
                tmp="";
            }
            if(tmp2.contains(ori+" -> ") && tmp2.contains(" -> "+dest)){
                s+=header;
                s+=tmp2;
                tmp1="";
                tmp2="";
                tmp="";
            }
            prev=b;
            act=c;
        }
        return exists;
    }
        
    
    /*************************** OUTROS ******************************/
    // Contrói o grafo 
    static void map_cities(ArrayList<Timetable> routes){
        for(Timetable r: routes){
            map.addTimetable(r.origin,r.destination,r);
        }
        
    }
    
    // Parser do ficheiro data.pl
    static void parse_data(){
        String line = "", temp = "", temp_split[], temp2_split[],arr="", dep="", orig="", dest="", days="", code="";
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(data);
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null){
                if(line!="\r"){
                    temp+=line;
                }
                if(line.contains(".")){
                    // retirar timetable
                    temp_split=temp.split("\\(");
                    temp=temp_split[1];
                    // guarda origem e destino
                    temp_split=temp.split(",", 3);
                    orig=temp_split[0];
                    dest=temp_split[1];
                    temp=temp_split[2];
                    // guarda os voos
                    temp_split=temp.split("\\s");
                    int i=0;
                    boolean stop=false;
                    ArrayList<Flight> dtemp=new ArrayList<Flight>();
                    while(true){
                        // se fôr parte da info
                        if(temp_split[i].contains("/")){
                            temp=temp_split[i];
                            temp2_split=temp.split("/");
                            dep=temp2_split[0].replaceAll("[^a-z 0-9 A-Z]","");
                            arr=temp2_split[1].replaceAll("[^a-z 0-9 A-Z]","");
                            code=temp2_split[2].replaceAll("[^a-z 0-9 A-Z]","");
                            days=temp2_split[3].replaceAll("[^a-z 0-9 A-Z]","");
                            Flight info=new Flight(days,code,dep,arr);
                            dtemp.add(info);
                        }
                        if(temp_split[i].contains(".")){
                                Timetable rout=new Timetable(orig,dest,dtemp);
                                routes.add(rout);
                                break;
                        }
                        i++;
                    }
                }
            }   
            System.out.println("Ficheiro lido com sucesso!");
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println("Verifique se tem o ficheiro de dados 'data.txt' no mesmo directório que o ficheiro java.");              
        }
        catch(IOException ex) {
            System.out.println(
                "Erro a ler o ficheiro '" 
                + data + "'");                  
        }
    }
}


// CONSTRÓI MAPA COMO GRAFO
class Graph {
    private HashMap<String, HashMap<String, Timetable>> map = new HashMap<String, HashMap<String, Timetable>>();

    void addCity(String city){
        if (map.containsKey(city))
            return;
        map.put(city, new HashMap<String,Timetable>());
    }
    
    
    boolean cityExists(String city){
        if(map.containsKey(city))
            return true;
        else
            return false;
    }
    
    Timetable getTimetable(String orig, String dest){
        HashMap<String, Timetable> adjacent = map.get(orig);
        Timetable r= adjacent.get(dest);
        return r;
    }  
    
    
    void addTimetable(String orig, String dest, Timetable r) {
        addCity(orig);
        addCity(dest);
        map.get(orig).put(dest,r);
    }
    
    boolean removeTimetable(String orig, String dest) {
        if (!map.containsKey(orig) || !map.containsKey(dest))
            return false;

        map.get(orig).remove(dest);
        return true;
    }
    
    boolean routeExists(String orig, String dest) {
        HashMap<String, Timetable> adjacent = map.get(orig);
        if(adjacent==null) {
            return false;
        }
        return adjacent.containsKey(dest);
    }
    
    LinkedList <String> next(String city){
        HashMap<String, Timetable> adjacent = map.get(city);
        LinkedList<String> keys = new LinkedList<String>();
        if(adjacent==null) {
            return new LinkedList<String>();
        }
        for(String key : adjacent.keySet()){
                keys.add(key);
        }
        return keys;
    }
}
