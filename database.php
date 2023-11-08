<?php
session_start();
if(isset($_POST['f']) && function_exists($_POST['f'])) {
    $_POST['f']();
 }
function logout(): void
{
    $_SESSION['user'] = null;
}
function connectBD():PDO{
    try {
        return new PDO('mysql:host=localhost;dbname=web;charset=utf8', 'root');
    } catch (Exception $e) {
        die('Erreur : ' . $e->getMessage());
    }
}
function login():void{
    $user = $_POST['user'];
    $password = $_POST['password'];
    // normalise the user name and password
    $user = htmlspecialchars($user);
    $password = htmlspecialchars($password);
    // hash the password with sha256
    $password = hash('sha256', $password);
    $db = connectBD()->query("SELECT * FROM user WHERE  username = '$user' AND password = '$password'");
    if ($db->rowCount() == 1) {
        $_SESSION['user'] = $db->fetch();
        echo "true";
    } else {
        echo "false";
    }
}
function register():void{
    $user = $_POST['user'];
    $password = $_POST['password'];
    $email = $_POST['email'];
    // check if all  the fields are filled
    if (empty($user) || empty($password) || empty($email)) {
        echo "false";
        return;
    }
    // normalise the user name and password
    $user = htmlspecialchars($user);
    $password = htmlspecialchars($password);
    $email = htmlspecialchars($email);
    // hash the password with sha256
    $password = hash('sha256', $password);
    try {
        $connexion = new PDO('mysql:host=localhost;dbname=web;charset=utf8', 'root');
    } catch (Exception $e) {
        die('Erreur : ' . $e->getMessage());
    }
    // check if the user already exist
    $db = $connexion->query("SELECT * FROM user WHERE username = '$user'");
    if ($db->rowCount() == 0) {
        $connexion->query("INSERT INTO user (username, password, mail,isAdmin) VALUES ('$user', '$password', '$email',0)");
        $_SESSION['user'] = $user;
        // get the id of the user
        $db = $connexion->query("SELECT * FROM user WHERE username = '$user'");
        $id = $db->fetch()['id'];
        // create the profile of the user
        $connexion->query("INSERT INTO profile (userId) VALUES ('$id')");
        echo "true";
    } else {
        echo "false";
    }

}
function resetPassword():void{
    $email = $_POST['email'];
    // TODO : send an email to the user with a link to reset the password

}
?>
