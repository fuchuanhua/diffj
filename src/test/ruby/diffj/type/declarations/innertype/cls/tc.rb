#!/usr/bin/jruby -w
# -*- ruby -*-

require 'diffj/type/declarations/innertype/tc'
require 'diffj/ast/type'

include Java

module DiffJ::Type::Declarations::InnerType::Cls
  class TestCase < DiffJ::Type::Declarations::InnerType::TestCase
    def added_msg_fmt
      DiffJ::TypeComparator::INNER_CLASS_ADDED
    end  

    def changed_msg_fmt
      raise "not implemented"
    end

    def removed_msg_fmt
      DiffJ::TypeComparator::INNER_CLASS_REMOVED
    end
  end
end
