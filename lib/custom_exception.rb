# frozen_string_literal: true

class CustomException < StandardError
  class InvalidInput < StandardError
    class Jar < StandardError
    end
  end
end
