#!/usr/bin/jruby -w
# -*- ruby -*-

require 'diffj/fdiff/writers/tc'
require 'diffj/fdiff/writers/ctx_no_hl'

include Java

class DiffJ::WriterContextTestCase < DiffJ::WriterTestCase
  def get_writer_class
    DiffJ::FDiff::Writer::ContextNoHighlightWriter
  end

  def test_change_print_from
    expected = make_expected FROMCONT, 2 .. 4, 5 .. 5, 6 .. 6
    run_change_test expected do |dw, str, fdc|
      dw.print_from str, fdc
    end
  end

  def test_change_print_to
    expected = make_expected TOCONT, 1 .. 3, 4 .. 4, 5 .. 7
    run_change_test expected do |dw, str, fdc|
      dw.print_to str, fdc
    end
  end

  def test_change_print_lines
    expfrom = make_expected FROMCONT, 2 .. 4, 5 .. 5, 6 .. 6
    expto   = make_expected TOCONT,   1 .. 3, 4 .. 4, 5 .. 7
    expected = expfrom + "\n" + expto + "\n"
    run_change_test expected do |dw, str, fdc|
      dw.print_lines str, fdc
    end
  end

  def make_expected lines, pre, match, post
    expected  = ""
    add_lines expected, lines, pre
    add_lines expected, lines, match, "!"
    add_lines expected, lines, post
    expected
  end

  def test_added_print_lines
    expected = make_expected TOCONT, 3 .. 5, 6 .. 7, nil
    expected << "\n"
    
    run_add_test expected do |dw, str, fda|
      dw.print_lines str, fda
    end
  end

  def test_deleted_print_lines
    expected = make_expected FROMCONT, 0 .. 0, 1 .. 1, 2 .. 4
    expected << "\n"
    
    run_delete_test expected do |dw, str, fdd|
      dw.print_lines str, fdd
    end
  end
end
