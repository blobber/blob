
class BuildrUtil
  
  def self.copy_libs(artifacts, prefix = ".")
    return false unless artifacts.instance_of?(Hash) and !artifacts.nil?
    
    artifacts.each do |artifact_string, data| next if data["dir"].nil? or data["dir"].empty?
      artifact = artifact_string.split(":")
      
      if artifact.size == 4
        file = "#{artifact[1]}-#{artifact[3]}.#{artifact[2]}" 
        dir  = "#{ENV['HOME']}/.m2/repository/#{artifact[0].gsub(".", "/")}/#{artifact[1]}/#{artifact[3]}"
      elsif artifact.size == 5
        file = "#{artifact[1]}-#{artifact[4]}-#{artifact[3]}.#{artifact[2]}" 
        dir  = "#{ENV['HOME']}/.m2/repository/#{artifact[0].gsub(".", "/")}/#{artifact[1]}/#{artifact[4]}"
      else
        raise "Don't know how to process artifact #{artifact_string}"
      end

      # add a prefix
      prefix  = "#{prefix}/" unless prefix.nil? or prefix[-1,1] == "/"

      # copy only if file doesn't exist
      data["dir"].each do |local_dir| 
        File.copy("#{dir}/#{file}", "#{prefix ||= ""}#{local_dir}/#{file}") unless File.exist?("#{local_dir}/#{file}") 
      end
    end
    
    return true
  end
  
  def self.classpath(artifacts)
    path = ""
    
    artifacts.each do |artifact, data| next if data["dir"].nil? or data["dir"].empty?
      artifact = artifact.split(":")
      file = "#{artifact[1]}-#{artifact[3]}.#{artifact[2]}"
      
      path << data["dir"].map { |local_dir| "#{local_dir}/#{file} " }.join(" ")
    end
    
    return path
  end
  
end