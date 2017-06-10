sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update && sudo apt-get upgrade -y && sudo apt-get install mysql-server -y && sudo apt-get install apache2 -y && sudo apt-get install oracle-java8-installer -y && sudo apt-get update
sudo mysql_secure_installation
mkdir tomcat && mkdir work 
sudo chmod +x install_stuff_2.sh
echo "Upload tomcat zip file to tomcat/ and sql+sh scripts to work/ then run install_stuff_2.sh"