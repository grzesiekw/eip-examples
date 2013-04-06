# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|

	config.vm.box = "ubuntu1204"
	config.vm.box_url = "https://www.dropbox.com/s/f4f5x5mejvoxreh/ubuntu1204.box"

	config.vm.network :private_network, ip: "192.168.10.10"

	# Share an additional folder to the guest VM. The first argument is
	# the path on the host to the actual folder. The second argument is
	# the path on the guest to mount the folder. And the optional third
	# argument is a set of non-required options.
	# config.vm.synced_folder "../data", "/vagrant_data"

	# Provider-specific configuration so you can fine-tune various
	# backing providers for Vagrant. These expose provider-specific options.
	# Example for VirtualBox:
	#
	config.vm.provider :virtualbox do |vb|
		vb.customize ["modifyvm", :id, "--cpus", 2, "--memory", "2048"]
	end

	config.vm.provision :chef_solo do |chef|
		chef.cookbooks_path = "cookbooks"

		chef.add_recipe "java"
		chef.add_recipe "apollo"
		chef.add_recipe "tomcat::ark"

		chef.json = {
				:java => {
						:jdk_version => '7',
						:install_flavor => 'oracle',
						:oracle => {
								:accept_oracle_download_terms => true
						},
						:jdk => {
								'7' => {
										:x86_64 => {
												:url => "http://10.0.2.2/resources/jdk-7u17-linux-x64.tar.gz",
												:checksum => "8611ce31e0b7ecb99d34703ad89b29a545a3fb30356553be3674366cbe722782"
										}
								}
						}
				},
				:tomcat => {
						:version => "7",
						:roles => ["manager-gui"],
						:users => [
								{:name => "tomcat", :password => "tomcat33", :roles => "manager-gui"}
						]
				},
				:apollo => {
						:broker => "eip"
				}
		}
	end
end
