# frozen_string_literal: true
require 'benchmark'
require 'sys-cpu'

class Benchmarker
  def initialize
    @results = []
  end

  def benchmark(name)
    time = Benchmark.realtime {yield}
    @result << {name: name, time:time}
    time
  end
  def cpu_load
    Sys::CPU.load_avg
  end
  def print
    @result.each do |result|
      puts "#{result[:name]} took #{result[:time].round(2)} seconds"
    end
  end
end

