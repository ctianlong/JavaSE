package test;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.vladsch.flexmark.ast.FencedCodeBlock;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.emoji.EmojiImageType;
import com.vladsch.flexmark.ext.emoji.EmojiShortcutType;
import com.vladsch.flexmark.ext.gfm.issues.GfmIssuesExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.gfm.users.GfmUsersExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.html.AttributeProvider;
import com.vladsch.flexmark.html.AttributeProviderFactory;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html.IndependentAttributeProviderFactory;
import com.vladsch.flexmark.html.renderer.AttributablePart;
import com.vladsch.flexmark.html.renderer.LinkResolverContext;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.*;
import com.vladsch.flexmark.util.data.MutableDataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.vladsch.flexmark.util.html.MutableAttributes;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import util.http.HttpClientUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author ctl
 * @date 2022/1/18
 */
public class MdTest {

	static class CodePreLineNumbersExtension implements HtmlRenderer.HtmlRendererExtension {
		@Override
		public void rendererOptions(@NotNull MutableDataHolder options) {
			// add any configuration settings to options you want to apply to everything, here
		}
		@Override
		public void extend(@NotNull HtmlRenderer.Builder htmlRendererBuilder, @NotNull String rendererType) {
			htmlRendererBuilder.attributeProviderFactory(CodePreLineNumbersAttributeProvider.Factory());
		}
		static CodePreLineNumbersExtension create() {
			return new CodePreLineNumbersExtension();
		}
	}

	static class CodePreLineNumbersAttributeProvider implements AttributeProvider {
		@Override
		public void setAttributes(@NotNull Node node, @NotNull AttributablePart part, @NotNull MutableAttributes attributes) {
			if (node instanceof FencedCodeBlock && part == AttributablePart.NODE) {
				attributes.addValue("class", "line-numbers");
			}
		}
		static AttributeProviderFactory Factory() {
			return new IndependentAttributeProviderFactory() {
				@NotNull
				@Override
				public AttributeProvider apply(@NotNull LinkResolverContext context) {
					return new CodePreLineNumbersAttributeProvider();
				}
			};
		}
	}


	@Test
	public void testFlexmark() throws Exception {
		String md = FileUtils.readFileToString(new File("C:\\Users\\ctl\\Desktop\\快应用分销API接口文档.md"), "UTF-8");
//		System.out.println(md);

		MutableDataSet options = new MutableDataSet().set(Parser.EXTENSIONS, Arrays.asList(
				CodePreLineNumbersExtension.create(),
				AutolinkExtension.create(),
				EmojiExtension.create(),
				StrikethroughExtension.create(),
				TaskListExtension.create(),
				TablesExtension.create(),
				TocExtension.create()
		))
				// set GitHub table parsing options
				.set(TablesExtension.WITH_CAPTION, false)
				.set(TablesExtension.COLUMN_SPANS, false)
				.set(TablesExtension.MIN_HEADER_ROWS, 1)
				.set(TablesExtension.MAX_HEADER_ROWS, 1)
				.set(TablesExtension.APPEND_MISSING_COLUMNS, true)
				.set(TablesExtension.DISCARD_EXTRA_COLUMNS, true)
				.set(TablesExtension.HEADER_SEPARATOR_COLUMN_MATCH, true)
				// setup emoji shortcut options
				// uncomment and change to your image directory for emoji images if you have it setup
//				.set(EmojiExtension.ROOT_IMAGE_PATH, emojiInstallDirectory())
				.set(EmojiExtension.USE_SHORTCUT_TYPE, EmojiShortcutType.GITHUB)
				.set(EmojiExtension.USE_IMAGE_TYPE, EmojiImageType.IMAGE_ONLY)
				// other options
				;
		// uncomment to convert soft-breaks to hard breaks
		//options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");
		Parser parser = Parser.builder(options).build();
		HtmlRenderer renderer = HtmlRenderer.builder(options).build();
		// You can re-use parser and renderer instances
		Document document = parser.parse(md);
		String markdownHtml = renderer.render(document);
//		System.out.println(markdownHtml);

		String template = FileUtils.readFileToString(new File("C:\\Users\\ctl\\Desktop\\markdown-template.html"), "UTF-8");
		String result = template.replace("${markdown-html}", markdownHtml);
		FileUtils.writeStringToFile(new File("C:\\Users\\ctl\\Desktop\\out-flexmark.html"), result, "UTF-8");
	}

	@Test
	public void testGithubMarkdownApi() throws IOException {
		String md = FileUtils.readFileToString(new File("C:\\Users\\ctl\\Desktop\\快应用分销API接口文档.md"), "UTF-8");
//		System.out.println(md);


		Map<String, Object> body = Maps.newHashMap();
		body.put("text", md);
		body.put("mode", "gfm");
		HttpClientUtils.Response response = HttpClientUtils.post("https://api.github.com/markdown", ImmutableMap.of("accept", "application/vnd.github.v3+json"),
				null, JSON.toJSONString(body));
//		System.out.println(response.getResponseBody());
		String markdownHtml = response.getResponseBody();

		String template = FileUtils.readFileToString(new File("C:\\Users\\ctl\\Desktop\\markdown-template.html"), "UTF-8");
		String result = template.replace("${markdown-html}", markdownHtml);
		FileUtils.writeStringToFile(new File("C:\\Users\\ctl\\Desktop\\out-githubapi.html"), result, "UTF-8");
	}

	@Test
	public void testGfmUsersIssue() {
		MutableDataHolder options = new MutableDataSet()
				.set(Parser.EXTENSIONS, Arrays.asList(StrikethroughExtension.create(), GfmUsersExtension.create(), GfmIssuesExtension.create(), AutolinkExtension.create()));
		Parser parser = Parser.builder(options).build();
		Document document = parser.parse("Hello, @world, and #1!");
		new NodeVisitor(new VisitHandler<?>[] { }) {
			@Override
			public void processNode(@NotNull Node node, boolean withChildren, @NotNull BiConsumer<Node, Visitor<Node>> processor) {
				System.out.println("Node: " + node);
				super.processNode(node, withChildren, processor);
			}
		}.visit(document);

		HtmlRenderer renderer = HtmlRenderer.builder(options).build();
		String html = renderer.render(document);
		System.out.println(html);
	}


}
