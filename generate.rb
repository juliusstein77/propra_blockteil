#!/usr/bin/env ruby-3.2.2
# frozen_string_literal: true

def generate_random_strings(length, count)
  strings = []
  count.times do |i|
    random_string = String.new
    l = (1..length).to_a.sample
    l.times { random_string << %w[A T G C].sample } # generates random strings with 'A', 'T', 'G', 'C'
    strings << random_string
  end
  strings
end


def generate_pairs(count)
  pairs = []
  (1..count).each do |i|
    ((i + 1)..count).each do |j|
      pairs << "#{i} #{j}"
    end
  end
  pairs
end

length = 14
count = 100
file_name = "./var/example/random_sequences.seqlib"
pair_file_name = "./var/example/random_sequences.pairs"
random_strings = generate_random_strings(length, count)
c = 0
pairs = generate_pairs(count)
File.open(pair_file_name, "w") do |file|
  pairs.each { |pair| file.puts pair }
end
File.open(file_name, "w") do |file|
  random_strings.each { |str| file.puts "#{c += 1}:#{str}" }
end
