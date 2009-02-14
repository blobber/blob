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
repositories.remote << 'https://admin:r3p0m4st3r@ben.vast.com/archiva/repository/internal'
 
desc 'Blob - Distributed Something :)' 
define 'blob' do  
  manifest['Copyright']  = 'Aleksandar Bradic, Igor Bogicevic 2009' 
  compile.options.target = '1.5'
  
  project.group = "blob"
  project.version = "0.0.1"
  
  # ARTIFACTS
  ARTIFACTS = {
    'org.apache.log4j:log4j:jar:1.2.15'                              => { "dir" => ["lib/"] },
    'testng:testng:jar:5.7-jdk15'                                    => { "dir" => ["lib/"] },
    'org.apache.commons.collections:commons-collections:jar:3.1'     => { "dir" => ["lib/"] },
    'org.apache.commons.logging:commons-logging:jar:1.0-unknown'     => { "dir" => ["lib/"] },
    'commons-cli:commons-cli:jar:2.0-SNAPSHOT'                       => { "dir" => ["lib/"] },
    'org.opengis:geoapi-nogenerics:jar:2.1-M2'                       => { "dir" => ["lib/"] },
    'org.geotools:gt:jar:2-referencing-2.3.1'                        => { "dir" => ["lib/"] },
    'javax.units:jsr:jar:108-0.01'                                   => { "dir" => ["lib/"] },
    'com.pjaol.search.solr:localsolr:jar:1.0-unknown'                => { "dir" => ["lib/"] },
    'org.apache.tools:ant:jar:1.6.5'                                 => { "dir" => ["lib/"] },
    'org.apache.solr.client.solrj:apache-solr-solrj:jar:1.3-dev'     => { "dir" => ["lib/"] },
    'org.apache.solr.common:apache-solr-common:jar:1.3-dev'          => { "dir" => ["lib/"] },
    'org.apache.solr:apache-solr-core:jar:1.3-dev'                   => { "dir" => ["lib/"] },
    'org.apache.commons.codec:commons-codec:jar:1.3'                 => { "dir" => ["lib/"] },
    'org.apache.commons.csv:commons-csv:jar:1.0-SNAPSHOT-r609327'    => { "dir" => ["lib/"] },
    'org.apache.commons.fileupload:commons-fileupload:jar:1.2'       => { "dir" => ["lib/"] },
    'org.apache.commons.io:commons-io:jar:1.3.1'                     => { "dir" => ["lib/"] },
    'org.eclipse.jdt:core:jar:3.1.1'                                 => { "dir" => ["lib/"] },
    'org.easymock:easymock:jar:1.0-unknown'                          => { "dir" => ["lib/"] },
    'org.mortbay:jetty:jar:6.1.3'                                    => { "dir" => ["lib/"] },
    'org.mortbay:jetty-util:jar:6.1.3'                               => { "dir" => ["lib/"] },
    'jsp:jsp:jar:2.1'                                                => { "dir" => ["lib/"] },
    'jsp-api:jsp-api:jar:2.1'                                        => { "dir" => ["lib/"] },
    'junit:junit:jar:4.3'                                            => { "dir" => ["lib/"] },
    'locallucene:locallucene:jar:1.0-unknown'                        => { "dir" => ["lib/"] },
    'com.pjaol.search.solr:localsolr:jar:1.0-unknown'                => { "dir" => ["lib/"] },
    'org.apache.lucene.analysis:lucene-analyzers:jar:2.4-dev'        => { "dir" => ["lib/"] },
    'org.apache.lucene:lucene-core:jar:2.4-dev'                      => { "dir" => ["lib/"] },
    'org.apache.lucene:lucene-highlighter:jar:2.4-dev'               => { "dir" => ["lib/"] },
    'org.apache.lucene.search:lucene-queries:jar:2.4-dev'            => { "dir" => ["lib/"] },
    'lucene-snowball:lucene-snowball:jar:2.4-dev'                    => { "dir" => ["lib/"] },
    'org.apache.lucene.search.spell:lucene-spellchecker:jar:2.4-dev' => { "dir" => ["lib/"] },
    'javax.servlet:servlet-api:jar:2.4'                              => { "dir" => ["lib/"] },
    'javax.servlet:servlet-api:jar:2.5-6.1.3'                        => { "dir" => ["lib/"] },
    'stax:stax:jar:1.2.0-dev'                                        => { "dir" => ["lib/"] },
    'javax.xml:stax-api:jar:1.0'                                     => { "dir" => ["lib/"] },
    'javanet.staxutils:stax-utils:jar:1.0-unknown'                   => { "dir" => ["lib/"] },
    'com.whirlycott.cache:whirlycache:jar:1.0.1-vast'                => { "dir" => ["lib/"] },
    'org.apache.commons.httpclient:commons-httpclient:jar:3.1'       => { "dir" => ["lib/"] },
    'org.mozilla:js:jar:1.0'                                         => { "dir" => ["lib/"] },
    'org.apache:zookeeper:jar:3.1.0'                                 => { "dir" => ["lib/"] },
    'commons-cli:commons-cli:jar:2.0-SNAPSHOT'                       => { "dir" => ["lib/"] },
    'org.apache.commons.codec:commons-codec:jar:1.3'                 => { "dir" => ["lib/"] },
    'org.apache.commons.logging:commons-logging:jar:1.0.4'           => { "dir" => ["lib/"] },
    'org.apache.commons.logging:commons-logging-api:jar:1.0.4'       => { "dir" => ["lib/"] },
    'org.apache.commons.lang:commons-lang:jar:2.4'                   => { "dir" => ["lib/"] },  
    'org.apache.hadoop:hadoop:jar:0.18.1-core'                       => { "dir" => ["lib/"] },
    'org.kosmix.kosmosfs.access:kfs:jar:0.1'                         => { "dir" => ["lib/"] },
    'org.apache.log4j:log4j:jar:1.2.13'                              => { "dir" => ["lib/"] },
    'org.znerd.xmlenc:xmlenc:jar:0.52'                               => { "dir" => ["lib/"] },
    'com.facebook.thrift:libthrift:jar:20080411p1'                   => { "dir" => ["lib/"] },
    'testng:testng:jar:5.7-jdk15'                                    => { "dir" => ["lib/"] },
    'org.codehaus.stax2:stax2:jar:2.1'                               => { "dir" => ["lib/"] },
    'woodstox.codehaus.org:wstx-api:jar:3.2.7'                       => { "dir" => ["lib/"] },
    'woodstox.codehaus.org:wstx-asl:jar:3.2.7'                       => { "dir" => ["lib/"] },
    'javax.xml:stax-api:jar:1.0.1'                                   => { "dir" => ["lib/"] },
    'org.apache.commons.httpclient:commons-httpclient:jar:3.1'       => { "dir" => ["lib/"] },
    'org.apache.commons.csv:commons-csv:jar:1.0-SNAPSHOT'            => { "dir" => ["lib/"] },
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
