#!/usr/bin/env ruby-3.3.2
require_relative 'lib/custom_exception.rb'
require_relative 'lib/exception_handler.rb'
require_relative 'jar_benchmarker.rb'
require 'yaml'


CustomExceptionHandler.handle_custom_exceptions do
  args = ARGV.dup
  if args.first == "jar"
    args.shift(1)
    config_file = args.first
    raise CustomException::InvalidInput::Config if config_file.nil?
    config = YAML.load_file(config_file)
    raise CustomException::InvalidInput if config.nil?
    jar_benchmarker = JarBenchmarker.new(config)
    jar_benchmarker.run
    jar_benchmarker.to_csv
  else
    raise CustomException::InvalidInput
  end
end