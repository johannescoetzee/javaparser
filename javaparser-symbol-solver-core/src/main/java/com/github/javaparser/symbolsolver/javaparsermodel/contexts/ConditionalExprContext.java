/*
 * Copyright (C) 2015-2016 Federico Tomassetti
 * Copyright (C) 2017-2024 The JavaParser Team.
 *
 * This file is part of JavaParser.
 *
 * JavaParser can be used either under the terms of
 * a) the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * b) the terms of the Apache License
 *
 * You should have received a copy of both licenses in LICENCE.LGPL and
 * LICENCE.APACHE. Please refer to those files for details.
 *
 * JavaParser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 */

package com.github.javaparser.symbolsolver.javaparsermodel.contexts;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.TypePatternExpr;
import com.github.javaparser.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.javaparsermodel.PatternVariableResult;
import com.github.javaparser.symbolsolver.javaparsermodel.PatternVariableVisitor;
import java.util.LinkedList;
import java.util.List;

public class ConditionalExprContext extends ExpressionContext<ConditionalExpr> {

    public ConditionalExprContext(ConditionalExpr wrappedNode, TypeSolver typeSolver) {
        super(wrappedNode, typeSolver);
    }

    /**
     * The following rules apply to a conditional expression a ? b : c:
     * - A pattern variable introduced by a when true is definitely matched at b.
     * - A pattern variable introduced by a when false is definitely matched at c.
     *
     * https://docs.oracle.com/javase/specs/jls/se21/html/jls-6.html#jls-6.3.1.4
     */
    @Override
    public List<TypePatternExpr> typePatternExprsExposedToChild(Node child) {
        List<TypePatternExpr> results = new LinkedList<>();

        PatternVariableVisitor variableVisitor = PatternVariableVisitor.getInstance();
        PatternVariableResult patternsInScope = wrappedNode.getCondition().accept(variableVisitor, null);

        if (wrappedNode.getThenExpr().containsWithinRange(child)) {
            results.addAll(patternsInScope.getVariablesIntroducedIfTrue());
        } else if (wrappedNode.getElseExpr().containsWithinRange(child)) {
            results.addAll(patternsInScope.getVariablesIntroducedIfFalse());
        }

        return results;
    }
}