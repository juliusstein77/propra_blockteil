# frozen_string_literal: true
require 'benchmark'
require 'sys-cpu'

class Benchmarker
  def initialize
    @results = []
  end

  def benchmark(name, uri = nil, iterations = 1)
    puts "Benchmarking #{uri}..."
    iterations.times do
      time = Benchmark.realtime { yield }
      cpu = cpu_load
      @results << { id:"#{uri}_#{Time.now.to_i}", name: name, time: time, cpu: cpu }
      time
    end
  end

  def cpu_load()
    return Sys::CPU.load_avg
  end

  def print
    results.each do |result|
      puts "#{result[:id]} \t CPU time: #{result[:time]} sec. \t CPU load: #{result[:cpu]}"
    end
  end
  def to_csv
    file = File.open("var/results/#{Time.now.to_i}.csv", "w")
    file.write("id, time, cpu, name\n")
    results.each do |result|
      file.write("#{result[:id]}, #{result[:time]}, #{result[:cpu]}, #{result[:name]}\n")
    end
    file.close
  end

  private
  def results
    @results.sort_by { |result| result[:id] }
  end
end

