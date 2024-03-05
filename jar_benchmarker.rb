# frozen_string_literal: true
require_relative 'benchmarker.rb'
require 'dotenv/load'
class JarBenchmarker < Benchmarker
  def initialize(jar_files)
    super()
    @jar_files = jar_files
  end
  def benchmark_jars
    @jar_files.each do |jar, main_class|
      benchmark(jar) do
        command = "#{ENV["JAVA_PATH"]}java.exe -jar #{jar}"
        system(command)
      end
    end
  end
end
