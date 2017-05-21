sudo mkdir /run/lock
sudo mkdir /var/log/apache2/
sudo touch /var/log/apache2/access.log
sudo touch /var/log/apache2/error.log
sudo touch /var/log/apache2/other_vhosts_access.log
sudo touch /var/log/apache2/suexec.log
sudo chown -R root:adm /var/log/apache2/
sudo chmod -R 750 /var/log/apache2
/etc/init.d/apache2 start
/etc/init.d/apache2 status
netstat -an | more 