# frozen_string_literal: true
module CustomExceptionHandler
  module_function
  def render_error(message)
    puts "[#{Time.now} ERROR] #{message}"
  end

  def handle_custom_exceptions
    yield
  rescue CustomException::InvalidInput::Jar => e
    render_error("Usage: ruby #{__FILE__} jar <jar_file> <main_class> [<jar_file> <main_class>]* - #{e.message}")

  rescue CustomException::InvalidInput::Config => e
    render_error("Usage: ruby #{__FILE__} jar <config_file> - #{e.message}")

  rescue CustomException::InvalidInput => e
    render_error("Usage: ruby #{__FILE__} <mode> <case_file> [<case_class>] - #{e.message}")

  end
end
