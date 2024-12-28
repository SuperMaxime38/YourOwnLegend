in vec2 pass_textureCoords;

uniform sampler2D textureSampler;

out vec4 outputColor;

void main(void) {
	outputColor = texture(textureSampler, pass_textureCoords);
}