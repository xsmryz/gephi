/*
 Copyright 2008-2011 Gephi
 Authors : Mathieu Bastian
 Website : http://www.gephi.org

 This file is part of Gephi.

 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

 Copyright 2011 Gephi Consortium. All rights reserved.

 The contents of this file are subject to the terms of either the GNU
 General Public License Version 3 only ("GPL") or the Common
 Development and Distribution License("CDDL") (collectively, the
 "License"). You may not use this file except in compliance with the
 License. You can obtain a copy of the License at
 http://gephi.org/about/legal/license-notice/
 or /cddl-1.0.txt and /gpl-3.0.txt. See the License for the
 specific language governing permissions and limitations under the
 License.  When distributing the software, include this License Header
 Notice in each file and include the License files at
 /cddl-1.0.txt and /gpl-3.0.txt. If applicable, add the following below the
 License Header, with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"

 If you wish your version of this file to be governed by only the CDDL
 or only the GPL Version 3, indicate your decision by adding
 "[Contributor] elects to include this software in this distribution
 under the [CDDL or GPL Version 3] license." If you do not indicate a
 single choice of license, a recipient has the option to distribute
 your version of this file under either the CDDL, the GPL Version 3 or
 to extend the choice of license to its licensees as provided above.
 However, if you add GPL Version 3 code and therefore, elected the GPL
 Version 3 license, then the option applies only if the new code is
 made subject to such option by the copyright holder.

 Contributor(s):

 Portions Copyrighted 2011 Gephi Consortium.
 */
package org.gephi.preview;

import java.awt.Graphics2D;
import java.awt.Image;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.ProcessingTarget;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.api.Vector;
import org.gephi.preview.spi.RenderTargetBuilder;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Mathieu Bastian
 */
@ServiceProvider(service = RenderTargetBuilder.class)
public class ProcessingRenderTargetBuilder implements RenderTargetBuilder {

    @Override
    public RenderTarget buildRenderTarget(PreviewModel previewModel) {
        Integer width = previewModel.getProperties().getValue("width");
        Integer height = previewModel.getProperties().getValue("height");
        if (width != null && height != null) {
            width = Math.max(1, width);
            height = Math.max(1, height);
            return new ProcessingTargetImpl(previewModel, width, height);
        } else {
            return new ProcessingTargetImpl(previewModel, 1, 1);
        }
    }

    @Override
    public String getName() {
        return RenderTarget.PROCESSING_TARGET;
    }

    public static class ProcessingTargetImpl extends AbstractRenderTarget implements ProcessingTarget {

        private final PreviewController previewController;
        private final PreviewModel previewModel;
        private ProcessingGraphics graphics;

        public ProcessingTargetImpl(PreviewModel model, int width, int height) {
            graphics = new ProcessingGraphics(width, height);
            previewController = Lookup.getDefault().lookup(PreviewController.class);
            previewModel = model;
        }

        @Override
        public void resize(int width, int height) {
            width = Math.max(1, width);
            height = Math.max(1, height);
            graphics.getGraphics().dispose();
            graphics = new ProcessingGraphics(width, height);
        }

        @Override
        public Graphics2D getGraphics() {
            return graphics.getGraphics();
        }

        @Override
        public Image getImage() {
            return graphics.getImage();
        }

        @Override
        public int getWidth() {
            return graphics.getWidth();
        }

        @Override
        public int getHeight() {
            return graphics.getHeight();
        }

        @Override
        public Vector getTranslate() {
            return graphics.getTranslate();
        }

        @Override
        public float getScaling() {
            return graphics.getScaling();
        }

        @Override
        public void setScaling(float scaling) {
            graphics.setScaling(scaling);
        }

        @Override
        public void setMoving(boolean moving) {
            previewModel.getProperties().putValue(PreviewProperty.MOVING, moving);
        }

        @Override
        public void refresh() {
            if (graphics != null) {
                graphics.refresh(previewController.getModel(), this);
            }
        }
    }
}
