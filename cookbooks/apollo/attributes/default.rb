default['apollo']['version'] = "1.6"
version = node['apollo']["version"]
default['apollo']['home'] = "/usr/local/apollo/default"
default['apollo']['broker_home'] = "/var/lib"
default['apollo']['broker'] = "broker"
default['apollo']['user'] = "apollo"

default['apollo'][version]['url'] = "http://ftp.ps.pl/pub/apache/activemq/activemq-apollo/1.6/apache-apollo-1.6-unix-distro.tar.gz"
checksum node['apollo'][version]['checksum'] = "2710425aea8642df60293b823e96e87e1123d5dccb78b9d311c94a0918a0b3d1"