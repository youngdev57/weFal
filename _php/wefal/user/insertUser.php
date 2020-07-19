 <?php  
    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('../db_conn.php'); 
 
    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {
        // 안드로이드 코드의 postParameters 변수에 적어준 이름을 가지고 값을 전달 받습니다. 
        $email=$_POST['email'];
        $pwd=$_POST['pwd'];
        
        /* 이메일 중복 검사 */
        try{
            $isEmailCheck = false; 
            
            $conn = mysqli_connect("localhost", "missing", "wefal1025!", "missing");
            $sql = "SELECT email FROM missing.test WHERE email = '{$email}'";
            $result_set = mysqli_query($conn, $sql); 
            $count = mysqli_num_rows($result_set);

            if($result_set) {    
                if($count == 0)
                {
                    echo "1";   // 생성 가능
                    $isEmailCheck = true;
                }
                else
                {
                    echo "-1";  // 중복
                    $isEmailCheck = false;
                    exit;
                }
            }
            // else 
            //     $errMSG = "error"; exit; 
        } catch(PDOException $e) {
            die("Database error: " . $e->getMessage());  
        }

        if($isEmailCheck) //!isset($errMSG) && 
        {
            try{
                // 테이블에 저장
                $stmt = $con->prepare('INSERT INTO missing.test(email, pwd) VALUES(:email, :pwd)');
                $stmt->bindParam(':email', $email);
                $stmt->bindParam(':pwd', $pwd);

                $stmt->execute();
                // if($stmt->execute()) 
                //     $successMSG = "Success to join"; 
                // else 
                //     $errMSG = "error"; 

            } catch(PDOException $e) {
                die("Database error: " . $e->getMessage());
            }
        } 
    } 
?>


<?php 
    mysqli_close($conn);
    //if (isset($errMSG)) echo $errMSG;
    //if (isset($successMSG)) echo $successMSG;

	$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
   
    if( !$android )
    {
        
    }
?>