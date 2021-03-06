#!/usr/bin/jruby -w
# -*- ruby -*-

require 'diffj/fdiff/writers/context'
require 'riel/text'

module DiffJ
  module FDiff
    module Writer
      class ContextHighlightWriter < ContextWriter
        include Loggable

        @@hl = ::Text::ANSIHighlighter.new
        
        RESET = "\e[0m"
        
        DEFAULT_FROM_COLOR_TEXT = "red"
        DEFAULT_TO_COLOR_TEXT = "yellow"

        DEFAULT_FROM_COLOR = @@hl.code DEFAULT_FROM_COLOR_TEXT
        DEFAULT_TO_COLOR = @@hl.code DEFAULT_TO_COLOR_TEXT

        def initialize from_contents, to_contents, color_deleted = nil, color_added = nil
          super from_contents, to_contents

          @color_deleted = (color_deleted && @@hl.code(color_deleted)) || DEFAULT_FROM_COLOR
          @color_added = (color_added && @@hl.code(color_added)) || DEFAULT_TO_COLOR
        end          

        def get_line lines, lidx, from_line, from_column, to_line, to_column, is_delete
          line = lines[lidx - 1]

          # PMD reports columns using tabSize == 8, so we replace tabs with
          # spaces here.
          # ... I loathe tabs.
          
          line = line.gsub "\t", " " * 8
          llen = line.length
          
          # columns are 1-indexed, strings are 0-indexed
          # ... half my life is adding or substracting one.
         
          fcol = from_line == lidx ? from_column - 1 : 0
          tcol = to_line   == lidx ? to_column       : llen

          highlight_color = is_delete ? @color_deleted : @color_added

          str = "! "
          str << line[0 ... fcol]
          str << highlight_color << line[fcol ... tcol] << RESET
          str << line[tcol ... llen] << EOLN
        end
      end
    end
  end
end
