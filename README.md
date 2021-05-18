# 1. 근사알고리즘

## 1.1) 근사알고리즘이란

### 1.1.1) NP완전

![img](https://upload.wikimedia.org/wikipedia/commons/4/4a/Complexity_classes.png)

간단하게 정리하면 시간복잡도가 다항식으로 표기할 수 있는 지 없는지가 중요하다.

1. NP문제  :  시간복잡도를 다항식으로 표기할 수 없는 문제, 다항 시간 내에 풀 수 없는 문제
2. P : 시간복잡도를 다항식으로 표기할 수 있는 문제, 다항 시간내에 풀 수 있는 문제
3. NP-완전 : 시간복잡도가 지수로 표현할 수 있는 문제 , 지수시간 내에 풀 수 있는 문제
4. 못 푸는 문제

### 1.1.2) NP완전문제 해결방법

1. 다항식 내에 해를 찾는것
2. 모든 입력에 대해 해를 찾는 것
3. 최적해를 찾는 것 

이것 들 중 한가지는 포기를 해야 해를 구할 수 있는데 3번을 포기하여 구하는 것, 최적해에 근사한 해를 구하는 것이 근사알고리즘이다.

문제의 크기가 작다면 bruteforce로 찾아낼 수 있다.

하지만 문제의 크기가 크다면 bruteforce로 모든 경우의 수를 구하기는 힘들기 때문에 근사알고리즘을 사용한다.



### 1.1.3) 근사알고리즘

근사알고리즘은 근사해를 찾는 대신에 다항식 시간복잡도를 가진다.

또한 근사해 이기 때문에 최적해와 얼만큼 근사한지 나타내는 근사비율을 제시해야한다.

이 근사비율은 1.0에 가까울수록 정확한 것이다.

위의 최적해란 간접최적해를 말한다.

(진짜 최적해를 찾기 위해서는 모든 경우의 수를 계산해야하는 모순이 생기기 때문이다.)

### 1.1.4) 근사알고리즘 대표문제

근사알고리즘으로 해결이 가능한 대표적인 문제로는

- 여행자문제(외판원)
- 정점커버문제
- 통 채우기 문제
- 작업스케쥴링 문제
- 클러스터링 문제

등이 있다.

본 프로젝트에서는 Jobscheduling문제에 대해 다루겠다.



# 2. JobScheduling구현코드

## 2.1) 설계과정

설계 과정을 크게 나누어보면

1. Greedy한 방법으로 작업을 나누어 입력한다.
2. bruteforce로 모든 작업을 나누어 모든 경우의 수를 찾는다.
3. greedy 한 방법과 bruteforce한 방법의 최종 시간을 비교한다.
4. 계속해서 반복해가면서 평균적인 근사도를 알아본다.

로 나눌 수 있다.

자세한 구현방법은 밑에서 코드와 함께 설명하겠습니다.

## 2.2) Jobscheduling

### 2.2.1) Greedy한 방법

```java
public static int Greedy(int [] machine, int [] task) {
        int m = machine.length;
        int n = task.length;
        int min = 0; //min 선언


        for(int i = 0; i<n; i++){
            min = 0;
            for(int j = 0; j<m;j++){
                if(machine[j]<machine[min]){
                    min = j;
                }
            }
            machine[min] += task[i]; 

        }

        int max = machine[0];
        for(int i =0; i<m;i++){
            if(max < machine[i]){
                max = machine[i];
            }
        }
        return max;
    }

```

1. machine과 task의 길이를 각각 m과 n에 저장한다.
2. 가장 빨리 끝나는 기계의 번호수를 저장할 min을 선언 및 0으로 초기화한다.
3. 제일 빨리 끝나는 기계에 작업을 배정한다.
4. machine배열의 각각 값들은 그 기계가 맡은 작업이 최종적으로 끝나는 시간이 들어있다.
5. 제일 오래걸리는 기계의 종료시간인 max를 구하여 반환





### 2.2.2)  randomtask- 작업의 길이를 랜덤하게 생성

 ```java
public static void randomtask(int [] task) { //랜덤으로 태스크를 만드는 배열
        int n = task.length;

        for(int i = 0; i<n;i++){
            task[i] = (int) (Math.random()*10+1); // 10이내의 랜덤값
        }

    }
 ```

1. 10이하의 작업을 랜덤하게 생성한다.



### 2.2.3) bruteforce 한 방법

 ```java
public static int bruteforce(int[] task, int [] machine){ 
        int m = machine.length;
        int n = task.length;
        int[] taskmachineNum = new int[n];
        int[] fintimearr = new int[(int) Math.pow(m, n)];


        int[][] alltaskmachinenum = alltaskmachineNum(machine,task);

        for (int i = 0; i < (int) Math.pow(m, n); i++) { // machine 초기화
            for (int j = 0; j < m; j++) {
                machine[j] = 0;
            }

            for (int k = 0; k < n; k++) { //작업을 기계에 넣는 과정

                int p = alltaskmachinenum[i][k]; // 작업이 들어갈 기계번호
                machine[p] += task[k];  // task길이를 기계의 종료시간에 추가연장해준다.
            }

            for (int l = 0; l < m; l++) {// 각 경우에서 가장 느렸던 종료시간을 fintimearr 저장
                if (machine[l] > fintimearr[i]) {
                    fintimearr[i] = machine[l];
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
 ```

1. 각 작업이 실행되는 기계가 몇번 기계인지 저장하는 배열 taskmachineNum를 선언한다.
2. 각 경우에서의 마지막 종료 시간을 저장하는 배열 fintimearr를 선언한다.
3. alltaskmachineNum 를 호출하여 모든 작업의 경우의 수를 이차원배열에 저장해둔다.
4. 이중포문으로 진입.
5. 한번 루프가 돌았다면 machine에 이전 작업결과들이 남아있을테니 다시 0으로 모두 초기화해준다.
6. 작업을 기계에 넣어준다.
7. 그 경우에서의 가장 느린 종료시간을 fintimearr에 저장한다.
8. 제일 작은 값 min 값을 반환한다.





### 2.2.4) alltaskmachineNum - 모든 경우의 수를 찾는 함수

 ```java
public static int[][] alltaskmachineNum(int[] machine, int[] task) { 

        int m = machine.length;
        int n = task.length;

        int [][] alltaskmachineNum = new int[(int) Math.pow(m, n)][n];
        int [] tmp = new int[n];


        for(int i = 0; i < (int) Math.pow(m, n); i++) {
            tmp[0]++;
            for (int k = 0; k < n; k++) {
                //그 줄의 배열을 alltask에 넣는 함수 추가
                if (m - 1 < tmp[k]) { 
                    //기계의 대수-1 보다 크면 0으로 넣어버린다. 만약 4개의 기계라면
                    tmp[k] = 0;      //기계번호 0 1 2 3 0 1 2 3 이런 순임.
                    if (k + 1 < n) tmp[k + 1]++; 
                    // 만약 이번 실행이 마지막이 아니라면 그 다음 기계번호매칭에 1을 더해서
                    // 다음기계에 일을 준다. 마치 진법처럼 그 위에것의 자리수가 올라간다.
                }
            }

            for(int j =0; j<n;j++){
                alltaskmachineNum[i][j] = tmp[j];
            }

        }

        return alltaskmachineNum;
    }
 ```

1. alltaskmachineNum 는 기계에 매치될 수 있는 모든 경우의 수를 구하는 함수 이다.
2. task의 개수 = 몇자리인지, machine의 개수 = 몇 진법 인지 로 생각하면 이해가 쉽다.
3. 자리수가 올라갈 때 그 윗자리가 1이 추가 되고 아랫자리는 다시 0이 되는 것처럼 구현하였다.





### 2.2.5) main함수

 ```java
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
 ```

**직접 측정해보니 gap을 A.length/2 로 계속 1/2로 줄여나가는 것 보다 **

**N-1/3이 더 빨라서 h = (A.length-1) / 3 로 작성하였다.**

이것에 대해서는 밑에서 다시 다루겠습니다.

### 2.2.6) 전체코드

```java
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
```





### 2.5) 출력결과

![123](C:\Users\Choi\Desktop\INU\2-1\컴퓨터알고리즘-김동훈\11주차\123.png)



bruteforce는 모든 경우의 수를 찾아보기 때문에 bruteforce가 더 빠를 수 밖에 없다.

하지만 greedy방법에서의 연산횟수가 훨씬 적다.



# 3. 성능비교

## 3.1) 근사 비율

```java
double sum=0;
for(int i = 0; i < 100; i++){
    for (int j = 0; j < machine.length; j++) {
        machine[j] = 0;
    }
    randomtask(task);
    double greedysol = Greedy(machine,task);
    double brutesol = bruteforce(task,machine);

    double rate = greedysol / brutesol;
    sum += rate;
}

System.out.println(sum/100);
```

메인함수 맨 뒷 부분에 코드를 추가해서

각각의 그리디/브루트포스 의 비율을 100회실행한 평균을 구해보았다.



|      | 4        | 8        | 10       | 12       | 14      | 16       |
| ---- | -------- | -------- | -------- | -------- | ------- | -------- |
| 2    | 1.092778 | 1.064187 | 1.048966 | 1.045064 | 1.04035 | 1.032923 |
| 4    | 1        | 1.171698 | 1.166431 | 초과     | 초과    | 초과     |
| 8    | x        | 1        | 초과     | 초과     | 초과    | 초과     |

표의 맨 윗 행은 작업의 개수, 맨 왼쪽 열은 기계의 대수를 나타낸다. 

작업의 개수가 많으면 많아질수록 점점 더 근사도가 올라간다는 것을 볼 수 있다.

또한 기계의 개수가 조금이라도 늘어나거나 작업의 개수가 늘어나면 메모리초과오류가 발생하기 때문에

이러한 문제를 해결할 때는 브루트포스방법 보다는 근사알고리즘을 사용하도록 해야겠다.

Approx Jobsheduling에서의 근사비율 

근사해는 OPT', 최적해는 OPT 이다.

OPT' <= 2 x OPT 이다. (근사해는 최적해의 2배를 넘지 않는다.)

![image-20210519022134695](C:\Users\Choi\AppData\Roaming\Typora\typora-user-images\image-20210519022134695.png)



자료출처 : ppt



## 3.2) Greedy 방법의 시간복잡도

for문들의 연산횟수를 보면

n x O(m)  + =>O(nm) 의 시간복잡도가 나온다.

## 3.3) Bruteforce 방법의 시간복잡도

for문이 alltaskmachinenum 을 호출하면서 O(m^n) x O(n) 이 나오고

brute force 에서 O(m^n) x {O(n)+O(m)+O(m)} 이 나온다.

지수의 시간복잡도가 나오므로 엄청난 시간이 걸린다.



## p.s.) 근사알고리즘을 짜면서 추가적으로 알게된 것들. 

1. 



