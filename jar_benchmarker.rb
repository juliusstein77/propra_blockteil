# frozen_string_literal: true

require_relative 'benchmarker.rb'
class JarBenchmarker < Benchmarker
  def initialize(jar_files)
    super()
    @jar_files = jar_files
    #puts @jar_files
  end
  def run
    tasks = []
    @jar_files.each do |jar|
      #p jar
      jar.each do |j|
        if j == "commands"
          next
        else

          java = j.find {|h| h.key?("java")}["java"]
          rounds = j.find {|h| h.key?("iterations")}["iterations"]
          cases = j.select {|h| h.key?("path")}
          cases.each do |cs|
            path = cs["path"]
            args = cs["args"]
            uri = ""
            command = "#{java}java.exe -jar #{path} "
            args.each do |k,v|
              if k == "--nw"
                command << "--nw "
                next
              elsif k == "--uri"
                uri = v
                next
              end
              command << "#{k} #{v} "
            end
            tasks << {uri: uri, command: command, rounds: rounds}
          end
        end
      end

      tasks.each do |task|
        benchmark(task[:command], task[:uri], task[:rounds]) do
          system("#{task[:command]} > #{Gem.win_platform? ? 'NUL' : '/dev/null'} 2>&1")
        end
      end
    end
  end
end
