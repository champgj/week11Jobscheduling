// 전에 작성했던 Jobscheduling
import java.util.*;

class Job{
    int start;
    int finished;
}
class JobScheduling {
    public static int L[][] = {{7,8},{3,7},{1,5},{5,9},{0,2},{6,8},{1,6}};
    public static ArrayList<Job>[] task = new ArrayList[5];
    public static int M=0;// 기계 수
    public static int Operating(int start){
        int i =0;

        if(M>0){
            for( i=0;i<M;i++){

                if(task[i].size()>0 && task[i].get(task[i].size()-1).finished<=start){
                    return i;
                }
            }
            M++;
            return i;
        }
        else{
            M++;
            return 0;
        }
    }
    public static void Scheduling(int n) {
        for(int j =0; j<5 ;j++){
            task[j] = new ArrayList<Job>();
        }

        int i = 0;
        while (i < n) {
            int machineNum = Operating(L[i][0]);
            task[machineNum].add(new Job());
            task[machineNum].get(task[machineNum].size()-1).start = L[i][0];
            task[machineNum].get(task[machineNum].size()-1).finished = L[i][1];
            i++;
        }
    }

    public static void main(String[] arg){
        int n = 7;
        Arrays.sort(L, (a,b) -> {
            if (a[0] != b[0]) return a[0] - b[0];
            else return a[1] - b[1];
        });
        Scheduling(n);
        System.out.println("작업의 수: "+n);
        System.out.println("기계의 수: "+M);

        for(int i =0;i<M;i++){
            if(task[i].size()!=0)
            {
                System.out.println("기계번호: "+i);
                for(int j =0;j<task[i].size();j++){
                    System.out.print("["+task[i].get(j).start+", "+task[i].get(j).finished+"]  ");
                }
                System.out.println();
            }
        }
    }
}