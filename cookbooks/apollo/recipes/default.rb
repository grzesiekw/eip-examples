include_recipe "ark"

version = node["apollo"]["version"]

user node['apollo']['user']

directory "/usr/local/apollo" do
	owner node['apollo']['user']
end

ark "apollo" do
	url node['apollo'][version]['url']
	checksum node['apollo'][version]['checksum']
	version node['apollo']['version']
	path  "/usr/local/apollo"
	home_dir node['apollo']['home']
	owner node['apollo']['user']
end

bash "apollo-create-broker" do
	code <<-END
		cd #{node['apollo']['broker_home']}
		#{node['apollo']['home']}/bin/apollo create #{node['apollo']['broker']}
		ln -s "#{node['apollo']['broker_home']}/#{node['apollo']['broker']}/bin/apollo-broker-service" /etc/init.d/
	END
end

template "#{node['apollo']['broker_home']}/#{node['apollo']['broker']}/etc/apollo.xml" do
	source "apollo.xml.erb"
	owner node['apollo']['user']
	mode "0644"
end

service "apollo-broker-service" do
	action [:enable, :start]
end