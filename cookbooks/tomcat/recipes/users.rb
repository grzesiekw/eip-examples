template "/etc/default/#{tomcat_version}" do
	source "default_tomcat.erb"
	owner "root"
	group "root"
	variables(:tomcat => node['tomcat'].to_hash)
	mode "0644"
	notifies :restart, "service[#{tomcat_version}]", :immediately
end