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

    public static int bruteforce(int[] task, int [] machine){ // 브루트포스 그냥 기계 대수에 나눠서 작업 분배
        int m = machine.length;
        int n = task.length;
        int[] taskmachineNum = new int[n];                //각 작업이 실행되는 기계가 몇번 기계인지 저장하는 배열
        int[] lastarr = new int[(int) Math.pow(m, n)];    //각 경우에서의 마지막 종료 시간을 저장하는 배열



        for (int i = 0; i < (int) Math.pow(m, n); i++) {
            zeromaker(machine); // 한번 루프가 돌았다면 machine에 이전 작업결과들이 남아있을테니 다시 0으로 모두 초기화해준다.

            for (int k = 0; k < n; k++) { //작업을 기계와 매치시키는 과정
                int p = taskmachineNum[k];
                machine[p] += task[k];  // task길이를 기계의 종료시간에 추가연장해준다.
            }

            taskmachineNum[0]++;
            for (int o = 0; o < n; o++) {    //작업이 들어갈 기계번호를 바꿔주는 과정
                if (m-1 < taskmachineNum[o]) {
                    taskmachineNum[o] = 0;
                    if (o + 1 < n) taskmachineNum[o + 1]++;

                }
            }


            for (int l = 0; l < m; l++) {// 각 경우에서 가장 느렸던 종료시간을 lastarr배열에 저장하는 과정
                if (machine[l] > lastarr[i]) { //만약 더 크면->더 느리면
                    lastarr[i] = machine[l];  // 그 값을 last arr에 저장
                }
            }

        }



        int min = 10000;
        for (int i = 0; i < (int) Math.pow(m, n); i++) {           //모든 경우에서 가장 작은 값 반환
            if (lastarr[i] < min) {
                min = lastarr[i];
            }
        }
        return min;


    }


    public static int[] zeromaker(int [] machine) {
        for (int j = 0; j < machine.length; j++) {
            machine[j] = 0; // 매 번 돌릴 때마다 기계의 마지막 종료시간을 다시 0으로 초기화 해준다.
        }
        return machine;
    }


    public static void main(String[] arg){

        System.out.print("입력의 개수와 기계대수를 입력하세요>");
        Scanner scanner = new Scanner(System.in);
        int totaltask = scanner.nextInt();
        int totalmachine = scanner.nextInt();

        int task[] = new int[totaltask];
        randomtask(task); //이렇게 하면 task배열의 값들이 모두 랜덤으로 설정된다.

        int machine[] = new int[totalmachine]; // 문제에서 기계댓수는 2개
        // 그리고 각각의 machine 배열에는 기계가 현재 하고 있는일이 끝나는시간이 들어가 있다.
        // ppt에서는 L로 표현을 하신 것 같다. 같은 것!

        // 랜덤이므로 생성된 배열의 값들을 한번 확인한다.
        System.out.println("랜덤생성된 작업들은");
        for (int i=0;i<task.length;i++){
            System.out.print(task[i]+" ");
        }
        System.out.println("");
        System.out.println("");
        System.out.println("greedy로 구현시 최종 걸리는 시간은 "+JobScheduling(machine,task)+"초 입니다.");


       for(int i = 0; i<machine.length;i++){
           machine[i]=0;
       }


        System.out.println("");
        System.out.println("bruteforce로 구현시 최종 걸리는 시간은 "+bruteforce(task,machine)+"초 입니다.");
    }
}