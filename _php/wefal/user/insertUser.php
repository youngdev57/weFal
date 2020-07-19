 <?php  
    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('db_conn.php');
 
    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {
        // 안드로이드 코드의 postParameters 변수에 적어준 이름을 가지고 값을 전달 받습니다. 
        $email=$_POST['email'];
        $pwd=$_POST['pwd'];

        if(empty($email)){
            $errMSG = "email을 입력하세요.";
        }
        else if(empty($pwd)){
            $errMSG = "패스워드를 입력하세요.";
        }

        if(!isset($errMSG)) // 이름과 나라 모두 입력이 되었다면 
        {
            try{
                // SQL문을 실행하여 데이터를 MySQL 서버의 person 테이블에 저장합니다. 
                $stmt = $con->prepare('INSERT INTO missing.test(email, pwd) VALUES(:email, :pwd)');
                $stmt->bindParam(':email', $email);
                $stmt->bindParam(':pwd', $pwd);

                if($stmt->execute())
                {
                    $successMSG = "새로운 TEST 튜플을 추가했습니다.";
                }
                else
                {
                    $errMSG = "추가 에러";
                }

            } catch(PDOException $e) {
                die("Database error: " . $e->getMessage()); 
            }
        } 
    } 
?>


<?php 
    if (isset($errMSG)) echo $errMSG;
    if (isset($successMSG)) echo $successMSG;

	$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
   
    if( !$android )
    {
?>
    <html>
       <body>
           aa
       </body>
    </html>

<?php 
    }
?>