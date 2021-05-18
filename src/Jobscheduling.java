// 전에 짰던 JobScheduling은 시작시간과 종료시간이 정해져있어서 시간상에서
// 시작시간과 종료시간을 비교해야했지만 이번에는 작업시간(일처리하는데 걸리는 시간)만 따지기 때문에
// 1차원배열로 해도 문제 없을 듯.

import java.util.Scanner;

class JobScheduling {
    public static int Greedy(int [] machine, int [] task) {
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
        int m = machine.length;     //기계대수
        int n = task.length;        //작업개수
        int[] taskmachineNum = new int[n];                //각 작업이 실행되는 기계가 몇번 기계인지 저장하는 배열
        int[] fintimearr = new int[(int) Math.pow(m, n)];    //각 경우에서의 마지막 종료 시간을 저장하는 배열


        int[][] alltaskmachinenum = alltaskmachineNum(machine,task); // 모든 작업의 경우의 수를 이차원배열에 저장해둔다.

        for (int i = 0; i < (int) Math.pow(m, n); i++) {
            for (int j = 0; j < m; j++) {// 한번 루프가 돌았다면 machine에 이전 작업결과들이 남아있을테니 다시 0으로 모두 초기화해준다.
                machine[j] = 0;
            }

            for (int k = 0; k < n; k++) { //작업을 기계에 넣는 과정

                int p = alltaskmachinenum[i][k]; // 작업이 들어갈 기계번호
                machine[p] += task[k];  // task길이를 기계의 종료시간에 추가연장해준다.
            }

            for (int l = 0; l < m; l++) {// 각 경우에서 가장 느렸던 종료시간을 fintimearr배열에 저장하는 과정
                if (machine[l] > fintimearr[i]) { //만약 더 크면->더 느리면
                    fintimearr[i] = machine[l];  // 그 값을 fintimearr에 저장
                }
            }

        }


        int min = 100000;

        for (int i = 0; i < (int) Math.pow(m, n); i++) { //모든 경우에서 가장 작은 값 반환
            if (fintimearr[i] < min) {
                min = fintimearr[i];
            }
        }

        return min;
    }



    public static int[][] alltaskmachineNum(int[] machine, int[] task) { //기계에 매치될 수 있는 모든 경우의 수를 구하는 함수

        int m = machine.length;
        int n = task.length;

        int [][] alltaskmachineNum = new int[(int) Math.pow(m, n)][n];
        int [] tmp = new int[n];


        for(int i = 0; i < (int) Math.pow(m, n); i++) {
            tmp[0]++;
            for (int k = 0; k < n; k++) {
                //그 줄의 배열을 alltask에 넣는 함수 추가
                if (m - 1 < tmp[k]) { //기계의 대수-1 보다 크면 0으로 넣어버린다. 만약 4개의 기계라면
                    tmp[k] = 0;      //기계번호 0 1 2 3 0 1 2 3 이런 순임.
                    if (k + 1 < n) tmp[k + 1]++; // 만약 이번 실행이 마지막이 아니라면 그 다음 기계번호매칭에 1을 더해서
                    // 다음기계에 일을 준다. 마치 진법처럼 그 위에것의 자리수가 올라간다.
                }
            }

            for(int j =0; j<n;j++){
                alltaskmachineNum[i][j] = tmp[j];
            }

        }

        return alltaskmachineNum;
    }


    public static void main(String[] arg){

        System.out.print("입력의 개수와 기계대수를 입력하세요>");
        Scanner scanner = new Scanner(System.in);
        int totaltask = scanner.nextInt();      //16;넣어서 테스트완료
        int totalmachine = scanner.nextInt();   //2;넣어서 테스트완료

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
        System.out.println("greedy로 구현시 최종 걸리는 시간은 "+Greedy(machine,task)+"초 입니다.");

       for(int i = 0; i<machine.length;i++){
           machine[i]=0;
       }

        System.out.println("");
        System.out.println("bruteforce로 구현시 최종 걸리는 시간은 "+bruteforce(task,machine)+"초 입니다.");

/*      각 경우의 근사도를 알아내는 코드!
        double sum=0;
        for(int i = 0; i < 100; i++){
            for (int j = 0; j < machine.length; j++) {// 한번 루프가 돌았다면 machine에 이전 작업결과들이 남아있을테니 다시 0으로 모두 초기화해준다.
                machine[j] = 0;
            }
            randomtask(task);
            double greedysol = Greedy(machine,task);
            double brutesol = bruteforce(task,machine);

            double rate = greedysol / brutesol;
            sum += rate;
        }

        System.out.println(sum/100);



 */

    }

}