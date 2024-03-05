require_relative 'lib/custom_exception.rb'
require_relative 'lib/exception_handler.rb'
require_relative 'jar_benchmarker.rb'
require 'dotenv/load'


CustomExceptionHandler.handle_custom_exceptions do
  if ARGV.first == "jar"
    jar_files = {}
    ARGV.drop(1).each_slice(2) do |file, main|
      jar_files[file] = main
    end

    if jar_files.empty?
      raise CustomException::InvalidInput::Jar
    end

    jar_benchmarker = JarBenchmarker.new(jar_files)
    jar_benchmarker.benchmark_jars
    #jar_benchmarker.print

  else
    raise CustomException::InvalidInput
  end
end