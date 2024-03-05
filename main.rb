require_relative 'lib/custom_exception.rb'
require_relative 'lib/exception_handler.rb'


CustomExceptionHandler.handle_custom_exceptions do
  if ARGV.first == "jar"
    jar_files = {}
    ARGV.drop(1).each_slice(2) do |file, main|
      jar_files[file] = main
    end

    if jar_files.empty?
      raise CustomException::InvalidInput::Jar
    end
  else
    raise CustomException::InvalidInput
  end
end