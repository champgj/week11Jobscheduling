// 전에 짰던 JobScheduling은 시작시간과 종료시간이 정해져있어서 시간상에서
// 시작시간과 종료시간을 비교해야했지만 이번에는 작업시간(일처리하는데 걸리는 시간)만 따지기 때문에
// 1차원배열로 해도 문제 없을 듯.

import java.util.Scanner;

class JobScheduling {
    public static int JobScheduling(int [] machine, int [] task) {
        int m = machine.length; // 기계대수 2를 m에 넣어둔다.
        int n = task.length; //task의 개수를 n에 넣어둔다.
        int min = 0; //min 선언


        for(int i = 0; i<n; i++){
            min = 0;
            for(int j = 0; j<m;j++){
                if(machine[j]<machine[min]){
                    min = j; //제일 빨리 끝나는 기계의 번호수이다. 기계가 두대밖에 없어서 상관이 있나?
                            //입력받는 거로 수정
                }
            }
            machine[min] += task[i]; // 작업 i를 제일 빨리 끝나는 기계에 배정한다.
                                        // 제일빨리 끝나는 기계의 종료시간에 태스크작업시간을 더해준다. 종료시간 연장!

        }

        int max = machine[0];
        for(int i =0; i<m;i++){
            if(max < machine[i]){
                max = machine[i];
            }

        }

        return max; // 제일 오래걸리는 기계의 종료시간을 반환
    }


    public static void randomtask(int [] task) { //랜덤으로 태스크를 만드는 배열
        int n = task.length;

        for(int i = 0; i<n;i++){
            task[i] = (int) (Math.random()*10+1); // 10이내의 랜덤값
        }

    }


    public static void bruteforce(int []machine, int [] task){ // 브루트포스 그냥 기계 대수에 나눠서 작업 분배
        int m = machine.length;
        int n = task.length;

        int[][] subtask = new int [(int) Math.pow(2,n)][n];
        int k = 0,p=0;
        for (int i = 0; i < (1<<n); i++)
        {
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) > 0) {
                    subtask[k][p] = task[j];
                    p++;
                }
            }
            p=0;
            k++;
        }
        System.out.println("모든 부분집합은");
        for(int i = 0; i<subtask.length;i++){
            for(int j=0;j<subtask[i].length;j++){
                if(subtask[i][j] != 0)
                    System.out.print(subtask[i][j]+" ");
            }
            System.out.println("");
        }

        int [] subtasklength = new int[(int) Math.pow(2,n)];

        int sum = 0;

        for(int i =0; i<(int) Math.pow(2,n);i++){
            for(int j = 0; j<n;j++){
                sum += subtask[i][j];
            }
            subtasklength[i] = sum;
            sum=0;

        }

        for(int i =0; i< subtasklength.length;i++){
            System.out.println(subtasklength[i]);
        }
        int min = 100;
        int memoryi=0;
        for(int i = 0; i<subtasklength.length/2;i++){
            if(min>Math.abs(subtasklength[i] - subtasklength[subtasklength.length-i-1])) {
                min = Math.abs(subtasklength[i] - subtasklength[subtasklength.length - i-1]);
                memoryi = i;
            }

        }
        System.out.println(memoryi+"번째의 subtasklength가 차이가 제일 작음");
        System.out.println("i번째 subtasklength는 일처리를 기계1이 ");
            for(int j=0;j<n;j++){
                if(subtask[memoryi][j] != 0)
                    System.out.print(subtask[memoryi][j]+" ");
            }
        System.out.println("만큼 하고 나머지 기계2가 ");
        for(int j=0;j<n;j++){
            if(subtask[subtask.length-memoryi-1][j] != 0)
                System.out.print(subtask[subtask.length-memoryi-1][j]+" ");
        }

        System.out.println("그래서 최종적으로 bruteforce 기계2대로 했을 때의 걸리는 시간은 ");
        int max = Math.max(subtasklength[memoryi],subtasklength[subtask.length - memoryi-1] );

        System.out.println(max + "초 이다");



    }



    public static void main(String[] arg){

        System.out.print("입력의 개수(2의 몇승)와 기계의 대수를 입력하세요>");
        Scanner scanner = new Scanner(System.in);
        int totaltask = scanner.nextInt();
        int totalmachine = scanner.nextInt();

        int task[] = new int[totaltask];
        randomtask(task); //이렇게 하면 task배열의 값들이 모두 랜덤으로 설정된다.

        int machine[] = new int[totalmachine]; // 입력받지만, 문제에서 기계댓수는 2개
        // 그리고 각각의 machineNum 배열에는 기계가 현재 하고 있는일이 끝나는시간이 들어가 있다.
        // ppt에서는 L로 표현을 하신 것 같다.

        // 랜덤이므로 생성된 배열의 값들을 한번 확인한다.
        System.out.println("랜덤생성된 작업의 길이들은");
        for (int i=0;i<task.length;i++){
            System.out.print(task[i]+" ");
        }
        System.out.println("");
        System.out.println("greedy로 구현시 최종 걸리는 시간은 "+JobScheduling(machine,task)+"초 입니다.");



       for(int i = 0; i<machine.length;i++){
           machine[i]=0;
       }

        System.out.println("");
       bruteforce(machine,task);





    }
}