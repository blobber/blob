require 'rubygems'
require 'log4r'
require 'buildr'

# HELPER CLASSES
Dir.glob(File.join(File.dirname(__FILE__), 'deps/*.rb')).each { |f| require f }

# BUILDR JVM OPTIONS
Java.classpath << "#{File.dirname(__FILE__)}/etc/"
Java.classpath << "/opt/hadoop/conf"

ENV['JAVA_OPTS'] ||= '-Xms512m -Xmx512m -ea'

# SET REPOS
repositories.remote << 'http://repo1.maven.org/maven2'
 
desc 'Blob - Distributed Something :)' 
define 'blob' do  
  manifest['Copyright']  = 'alek.br, caboom 2009' 
  compile.options.target = '1.5'
  
  project.group = "blob"
  project.version = "0.0.1"
  
  # ARTIFACTS
  ARTIFACTS = {
    'jmock:jmock:jar:1.2.0'                  => { "dir" => ["lib/"] },
    'org.slf4j:slf4j-api:jar:1.5.6'          => { "dir" => ["lib/"] },
    'org.slf4j:slf4j-log4j12:jar:1.5.6'      => { "dir" => ["lib/"] },
    'org.jyaml:jyaml:jar:1.3'                => { "dir" => ["lib/"] },
    'org.testng:testng:jar:jdk15:5.7'        => { "dir" => ["lib/"] }
  }
   
  #  
  # BUILDING/PACKAGING
  #
  
  compile.with ARTIFACTS.keys
  pack = package(:jar, :file=>_("dist/#{id}-#{version}.jar")).with
      
  build do 
    puts "Building Blob, version: #{project.version}" 
    BuildrUtil.copy_libs ARTIFACTS
  end
  
  #
  # TESTS
  #

  test.using :testng
  test.include 'org.blob.tests.*'

  test do
    puts "Have a good and happy life - fix bugs before that..."
  end
  
  #
  # CLEANUP
  # 
  
  clean do
    FileUtils.rm_r "dist/*"    rescue nil
    FileUtils.rm_r "package/*" rescue nil
  end
  
end
