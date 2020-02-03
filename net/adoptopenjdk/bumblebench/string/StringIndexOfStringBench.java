/*******************************************************************************
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*******************************************************************************/

package net.adoptopenjdk.bumblebench.string;

import java.util.Random;
import net.adoptopenjdk.bumblebench.core.MicroBench;

/**
 * \brief
 * 
 * Measures the performance of String.indexOf(String) using a fixed random
 * string generated from fixed seed random object.
 * 
 * \details There are 4 sub-string length settings and 4 sub-string index
 * settings. The sub-strings are at indices of approximately 10, 100, 1k, and
 * 50k. Their lengths are randomly chosen at around 10, 100, 1k, 10k
 * 
 * Use -DBumbleBench.stringLength=1 to 4 and -DBumbleBench.indexLevel=1 to 4 too
 * choose testing modes.
 */
public class StringIndexOfStringBench extends MicroBench {

	static final int MAX_NUM_STRINGS = option("maxNumStrings", 10);
	static int stringLength = option("stringLength", 1);
	static int indexLevel = option("indexLevel", 1);
	static boolean useRandomString = option("useRandomString", false);

	static String[] strings = new String[MAX_NUM_STRINGS];
	static int[] indices = new int[MAX_NUM_STRINGS];
	static int[] resultIndices = new int[MAX_NUM_STRINGS];

	static int currentIterationCount;
	static int minIndex, maxIndex;
	static int minLen, maxLen;

	private static String randomCompressible64KCharString;

	private static String wikipediaArticleString = "Main article: Name of Canada\r\n"
			+ "While a variety of theories have been postulated for the etymological origins of Canada, the name is now accepted as coming from the St. Lawrence Iroquoian word kanata, meaning \"village\" or \"settlement\".[12] In 1535, indigenous inhabitants of the present-day Quebec City region used the word to direct French explorer Jacques Cartier to the village of Stadacona.[13] Cartier later used the word Canada to refer not only to that particular village but to the entire area subject to Donnacona (the chief at Stadacona);[13] by 1545, European books and maps had begun referring to this small region along the Saint Lawrence River as Canada.[13]\r\n"
			+ "From the 16th to the early 18th century \"Canada\" referred to the part of New France that lay along the Saint Lawrence River.[14] In 1791, the area became two British colonies called Upper Canada and Lower Canada collectively named the Canadas; until their union as the British Province of Canada in 1841.[15] Upon Confederation in 1867, Canada was adopted as the legal name for the new country at the London Conference, and the word Dominion was conferred as the country's title.[16] The transition away from the use of Dominion was formally reflected in 1982 with the passage of the Canada Act, which refers only to Canada. Later that year, the name of the national holiday was changed from Dominion Day to Canada Day.[17] The term Dominion is also used to distinguish the federal government from the provinces, though after the Second World War the term federal had replaced dominion.[18]\r\n"
			+ "History\r\n" + "Main article: History of Canada\r\n"
			+ "See also: Timeline of Canadian history and List of years in Canada\r\n"
			+ "Further information: Historiography of Canada\r\n" + "Indigenous peoples\r\n"
			+ "Colour-coded map of North America showing the distribution of North American language families north of Mexico\r\n"
			+ "Linguistic areas of North American Indigenous peoples at the time of European contact\r\n"
			+ "Indigenous peoples in present-day Canada include the First Nations, Inuit, and Métis,[19] the last being a mixed-blood people who originated in the mid-17th century when First Nations and Inuit people married European settlers.[19] The term \"Aboriginal\" as a collective noun is a specific term of art used in some legal documents, including the Constitution Act 1982.[20]\r\n"
			+ "The first inhabitants of North America are generally hypothesized to have migrated from Siberia by way of the Bering land bridge[21] and arrived at least 14,000 years ago.[22] The Paleo-Indian archaeological sites at Old Crow Flats and Bluefish Caves are two of the oldest sites of human habitation in Canada.[23] The characteristics of Canadian indigenous societies included permanent settlements, agriculture, complex societal hierarchies, and trading networks.[24][25] Some of these cultures had collapsed by the time European explorers arrived in the late 15th and early 16th centuries and have only been discovered through archeological investigations.[26]\r\n"
			+ "The indigenous population at the time of the first European settlements is estimated to have been between 200,000[27] and two million,[28] with a figure of 500,000 accepted by Canada's Royal Commission on Aboriginal Peoples.[29] As a consequence of European colonization, the population of Canada's indigenous peoples declined by forty to eighty percent, and several First Nations, such as the Beothuk, disappeared.[30] The decline is attributed to several causes, including the transfer of European diseases, such as influenza, measles, and smallpox to which they had no natural immunity,[27][31] conflicts over the fur trade, conflicts with the colonial authorities and settlers, and the loss of indigenous lands to settlers and the subsequent collapse of several nations' self-sufficiency.[32][33]\r\n"
			+ "Although not without conflict, European Canadians' early interactions with First Nations and Inuit populations were relatively peaceful.[34] First Nations and Métis peoples played a critical part in the development of European colonies in Canada, particularly for their role in assisting European coureur des bois and voyageurs in the exploration of the continent during the North American fur trade.[35] The Crown and indigenous peoples began interactions during the European colonization period, though the Inuit, in general, had more limited interaction with European settlers.[36] However, from the late 18th century, European Canadians encouraged indigenous peoples to assimilate into their own culture.[37] These attempts reached a climax in the late 19th and early 20th centuries with forced integration and relocations.[38] A period of redress is underway, which started with the appointment of the Truth and Reconciliation Commission of Canada by the Government of Canada in 2008.[39]\r\n"
			+ "European colonization\r\n"
			+ "The first known attempt at European colonization began when Norsemen settled briefly at L'Anse aux Meadows in Newfoundland around 1000 AD.[40] No further European exploration occurred until 1497, when Italian seafarer John Cabot explored and claimed Canada's Atlantic coast in the name of King Henry VII of England.[41][42] Then Basque and Portuguese mariners established seasonal whaling and fishing outposts along the Atlantic coast in the early 16th century.[43] In 1534, French explorer Jacques Cartier explored the Gulf of Saint Lawrence where, on July 24, he planted a 10-metre (33 ft) cross bearing the words \"Long Live the King of France\" and took possession of the territory New France in the name of King Francis I.[44] In general the settlements appear to have been short-lived, possibly due to the similarity of outputs producible in Scandinavia and northern Canada and the problems of navigating trade routes at that time.[45]\r\n"
			+ "In 1583, Sir Humphrey Gilbert, by the royal prerogative of Queen Elizabeth I, founded St. John's, Newfoundland, as the first North American English colony.[46] French explorer Samuel de Champlain arrived in 1603 and established the first permanent European settlements at Port Royal (in 1605) and Quebec City (in 1608).[47] Among the colonists of New France, Canadiens extensively settled the Saint Lawrence River valley and Acadians settled the present-day Maritimes, while fur traders and Catholic missionaries explored the Great Lakes, Hudson Bay, and the Mississippi watershed to Louisiana.[48] The Beaver Wars broke out in the mid-17th century over control of the North American fur trade.[49]\r\n"
			+ "Benjamin West's \"The Death of General Wolfe\" dying in front of British flag while attended by officers and native allies\r\n"
			+ "Benjamin West's The Death of General Wolfe (1771) dramatizes James Wolfe's death during the Battle of the Plains of Abraham at Quebec.\r\n"
			+ "The English established additional settlements in Newfoundland, beginning in 1610[50] and the Thirteen Colonies to the south were founded soon after.[43] A series of four wars erupted in colonial North America between 1689 and 1763; the later wars of the period constituted the North American theatre of the Seven Years' War.[51] Mainland Nova Scotia came under British rule with the 1713 Treaty of Utrecht, and Canada and most of New France came under British rule in 1763 after the Seven Years' War.[52]\r\n"
			+ "The Royal Proclamation of 1763 established First Nation treaty rights, created the Province of Quebec out of New France, and annexed Cape Breton Island to Nova Scotia.[17] St. John's Island (now Prince Edward Island) became a separate colony in 1769.[53] To avert conflict in Quebec, the British Parliament passed the Quebec Act of 1774, expanding Quebec's territory to the Great Lakes and Ohio Valley. More importantly, the Quebec Act afforded Quebec special autonomy and rights of self-administration at a time that the Thirteen Colonies were increasingly agitating against British rule.[54] It re-established the French language, Catholic faith, and French civil law there, staving off the growth of an independence movement in contrast to the Thirteen Colonies. The Proclamation and the Quebec Act in turn angered many residents of the Thirteen Colonies, further fuelling anti-British sentiment in the years prior to the American Revolution.[17]\r\n"
			+ "After the successful American War of Independence, the 1783 Treaty of Paris recognized the independence of the newly formed United States and set the terms of peace, ceding British North American territories south of the Great Lakes to the new country.[55] The American war of independence also caused a large out-migration of Loyalists the settlers who had fought against American independence. Many moved to Canada, particularly Atlantic Canada, where their arrival changed the demographic distribution of the existing territories. New Brunswick was in turn split from Nova Scotia as part of a reorganization of Loyalist settlements in the Maritimes which led to the incorporation of Saint John, New Brunswick to become Canada's first city.[56] To accommodate the influx of English-speaking Loyalists in Central Canada, the Constitutional Act of 1791 divided the province of Canada into French-speaking Lower Canada (later Quebec) and English-speaking Upper Canada (later Ontario), granting each its own elected legislative assembly.[57]\r\n"
			+ "The Canadas were the main front in the War of 1812 between the United States and the United Kingdom. Peace came in 1815; no boundaries were changed. Immigration resumed at a higher level, with over 960,000 arrivals from Britain between 1815 50.[58] New arrivals included refugees escaping the Great Irish Famine as well as Gaelic-speaking Scots displaced by the Highland Clearances.[59] Infectious diseases killed between 25 and 33 percent of Europeans who immigrated to Canada before 1891.[27]\r\n"
			+ "The desire for responsible government resulted in the abortive Rebellions of 1837.[60] The Durham Report subsequently recommended responsible government and the assimilation of French Canadians into English culture.[17] The Act of Union merged the Canadas into a united Province of Canada and responsible government was established for all provinces of British North America by 1849.[61] The signing of the Oregon Treaty by Britain and the United States in 1846 ended the Oregon boundary dispute, extending the border westward along the 49th parallel. This paved the way for British colonies on Vancouver Island (1849) and in British Columbia (1858).[62] In 1867, the same year as Canadian Confederation, Britain declined to purchase for Canada the Alaska territory that was to that point tenuously held by Russia. With the United States purchasing Alaska instead, clearly demarcated borders for Canada, although there would continue to be some disputes about the exact demarcation of the Alaska-Yukon and Alaska-BC border for years to come.[63]\r\n"
			+ "Confederation and expansion\r\n" + "Refer to caption\r\n"
			+ "An animated map showing the growth and change of Canada's provinces and territories since Confederation in 1867\r\n"
			+ "Following several constitutional conferences, the Constitution Act officially proclaimed Canadian Confederation on July 1, 1867, initially with four provinces: Ontario, Quebec, Nova Scotia, and New Brunswick.[64][65] Canada assumed control of Rupert's Land and the North-Western Territory to form the Northwest Territories, where the Métis' grievances ignited the Red River Rebellion and the creation of the province of Manitoba in July 1870.[66] British Columbia and Vancouver Island (which had been united in 1866) joined the confederation in 1871, while Prince Edward Island joined in 1873.[67]\r\n"
			+ "To open the West to European immigration, parliament also approved sponsoring the construction of three transcontinental railways (including the Canadian Pacific Railway), opening the prairies to settlement with the Dominion Lands Act, and establishing the North-West Mounted Police to assert its authority over this territory.[68][69] In 1898, during the Klondike Gold Rush in the Northwest Territories, parliament created the Yukon Territory. Alberta and Saskatchewan became provinces in 1905.[67]\r\n"
			+ "Early 20th century\r\n" + "Group of armed soldiers marching past a wrecked tank and a body\r\n"
			+ "Canadian soldiers and a Mark II tank at the Battle of Vimy Ridge in 1917\r\n"
			+ "Because Britain still maintained control of Canada's foreign affairs under the Constitution Act, 1867, its declaration of war in 1914 automatically brought Canada into World War I.[70] Volunteers sent to the Western Front later became part of the Canadian Corps, which played a substantial role in the Battle of Vimy Ridge and other major engagements of the war.[71] Out of approximately 625,000 Canadians who served in World War I, some 60,000 were killed and another 172,000 were wounded.[72] The Conscription Crisis of 1917 erupted when the Unionist Cabinet's proposal to augment the military's dwindling number of active members with conscription was met with vehement objections from French-speaking Quebecers.[73] The Military Service Act brought in compulsory military service, though it, coupled with disputes over French language schools outside Quebec, deeply alienated Francophone Canadians and temporarily split the Liberal Party.[73] In 1919, Canada joined the League of Nations independently of Britain,[71] and the 1931 Statute of Westminster affirmed Canada's independence.[4]\r\n"
			+ "Crew of a Sherman-tank resting while parked\r\n"
			+ "Canadian crew of a Sherman tank, south of Vaucelles, France, during the Battle of Normandy in June 1944\r\n"
			+ "The Great Depression in Canada during the early 1930s saw an economic downturn, leading to hardship across the country.[74] In response to the downturn, the Co-operative Commonwealth Federation (CCF) in Saskatchewan introduced many elements of a welfare state (as pioneered by Tommy Douglas) in the 1940s and 1950s.[75] On the advice of Prime Minister William Lyon Mackenzie King, war with Germany was declared effective September 10, 1939, by King George VI, seven days after the United Kingdom. The delay underscored Canada's independence.[71]\r\n"
			+ "The first Canadian Army units arrived in Britain in December 1939. In all, over a million Canadians served in the armed forces during World War II and approximately 42,000 were killed and another 55,000 were wounded.[76] Canadian troops played important roles in many key battles of the war, including the failed 1942 Dieppe Raid, the Allied invasion of Italy, the Normandy landings, the Battle of Normandy, and the Battle of the Scheldt in 1944.[71] Canada provided asylum for the Dutch monarchy while that country was occupied and is credited by the Netherlands for major contributions to its liberation from Nazi Germany.[77]\r\n"
			+ "The Canadian economy boomed during the war as its industries manufactured military materiel for Canada, Britain, China, and the Soviet Union.[71] Despite another Conscription Crisis in Quebec in 1944, Canada finished the war with a large army and strong economy.[78]\r\n"
			+ "Contemporary era\r\n" + "Harold Alexander at desk receiving legislation\r\n"
			+ "At Rideau Hall, Governor General the Viscount Alexander of Tunis (centre) receives the bill finalizing the union of Newfoundland and Canada on March 31, 1949\r\n"
			+ "The financial crisis of the Great Depression had led the Dominion of Newfoundland to relinquish responsible government in 1934 and become a crown colony ruled by a British governor.[79] After two bitter referendums, Newfoundlanders voted to join Canada in 1949 as a province.[80]\r\n"
			+ "Canada's post-war economic growth, combined with the policies of successive Liberal governments, led to the emergence of a new Canadian identity, marked by the adoption of the Maple Leaf Flag in 1965,[81] the implementation of official bilingualism (English and French) in 1969,[82] and the institution of official multiculturalism in 1971.[83] Socially democratic programs were also instituted, such as Medicare, the Canada Pension Plan, and Canada Student Loans, though provincial governments, particularly Quebec and Alberta, opposed many of these as incursions into their jurisdictions.[84]\r\n"
			+ "Finally, another series of constitutional conferences resulted in the Canada Act, the patriation of Canada's constitution from the United Kingdom, concurrent with the creation of the Canadian Charter of Rights and Freedoms.[85][86][87] Canada had established complete sovereignty as an independent country, although the Queen retained her role as monarch of Canada.[88][89] In 1999, Nunavut became Canada's third territory after a series of negotiations with the federal government.[90]\r\n"
			+ "At the same time, Quebec underwent profound social and economic changes through the Quiet Revolution of the 1960s, giving birth to a secular nationalist movement.[91] The radical Front de libération du Québec (FLQ) ignited the October Crisis with a series of bombings and kidnappings in 1970[92] and the sovereignist Parti Québécois was elected in 1976, organizing an unsuccessful referendum on sovereignty-association in 1980. Attempts to accommodate Quebec nationalism constitutionally through the Meech Lake Accord failed in 1990.[93] This led to the formation of the Bloc Québécois in Quebec and the invigoration of the Reform Party of Canada in the West.[94][95] A second referendum followed in 1995, in which sovereignty was rejected by a slimmer margin of 50.6 to 49.4 percent.[96] In 1997, the Supreme Court ruled that unilateral secession by a province would be unconstitutional and the Clarity Act was passed by parliament, outlining the terms of a negotiated departure from Confederation.[93]\r\n"
			+ "In addition to the issues of Quebec sovereignty, a number of crises shook Canadian society in the late 1980s and early 1990s. These included the explosion of Air India Flight 182 in 1985, the largest mass murder in Canadian history;[97] the École Polytechnique massacre in 1989, a university shooting targeting female students;[98] and the Oka Crisis of 1990,[99] the first of a number of violent confrontations between the government and indigenous groups.[100] Canada also joined the Gulf War in 1990 as part of a US-led coalition force and was active in several peacekeeping missions in the 1990s, including the UNPROFOR mission in the former Yugoslavia.[101]\r\n"
			+ "Canada sent troops to Afghanistan in 2001, but declined to join the U.S.-led invasion of Iraq in 2003.[102] In 2011, Canadian forces participated in the NATO-led intervention into the Libyan Civil War,[103] and also became involved in battling the Islamic State insurgency in Iraq in the mid-2010s.[104]\r\n"
			+ "Geography and climate\r\n" + "Main articles: Geography of Canada and Climate of Canada\r\n"
			+ "Köppen climate types of Canada\r\n"
			+ "Canada occupies much of the continent of North America, sharing land borders with the contiguous United States to the south, and the U.S. state of Alaska to the northwest. Canada stretches from the Atlantic Ocean in the east to the Pacific Ocean in the west; to the north lies the Arctic Ocean.[105] Greenland is to the northeast and to the southeast Canada shares a maritime boundary with the Republic of France's overseas collectivity of Saint Pierre and Miquelon, the last vestige of New France.[106] By total area (including its waters), Canada is the second-largest country in the world, after Russia. By land area alone, however, Canada ranks fourth, the difference being due to it having the world's largest proportion of fresh water lakes.[107] Of Canada's thirteen provinces and territories, only two are landlocked (Alberta and Saskatchewan) while the other eleven all directly border one of three oceans.\r\n"
			+ "Canada is home to the world's northernmost settlement, Canadian Forces Station Alert, on the northern tip of Ellesmere Island   latitude 82.5°N   which lies 817 kilometres (508 mi) from the North Pole.[108] Much of the Canadian Arctic is covered by ice and permafrost. Canada has the longest coastline in the world, with a total length of 243,042 kilometres (151,019 mi);[109] additionally, its border with the United States is the world's longest land border, stretching 8,891 kilometres (5,525 mi).[110] Three of Canada's arctic islands, Baffin Island, Victoria Island and Ellesmere Island, are among the ten largest in the world.[111]\r\n"
			+ "Since the end of the last glacial period, Canada has consisted of eight distinct forest regions, including extensive boreal forest on the Canadian Shield.[112] Canada has over 2,000,000 lakes 563 greater than 100 km2 (39 sq mi) which is more than any other country, containing much of the world's fresh water.[113][114] There are also fresh-water glaciers in the Canadian Rockies and the Coast Mountains.[115]\r\n"
			+ "The Mount Meager massif as seen from the east near Pemberton. Summits left to right are Capricorn Mountain, Mount Meager and Plinth Peak.\r\n"
			+ "Canada is geologically active, having many earthquakes and potentially active volcanoes, notably Mount Meager massif, Mount Garibaldi, Mount Cayley massif, and the Mount Edziza volcanic complex.[116] The volcanic eruption of the Tseax Cone in 1775 was among Canada's worst natural disasters, killing an estimated 2,000 Nisga'a people and destroying their village in the Nass River valley of northern British Columbia.[117] The eruption produced a 22.5-kilometre (14.0 mi) lava flow, and, according to Nisga'a legend, blocked the flow of the Nass River.[118]\r\n"
			+ "Average winter and summer high temperatures across Canada vary from region to region. Winters can be harsh in many parts of the country, particularly in the interior and Prairie provinces, which experience a continental climate, where daily average temperatures are near  15 °C (5 °F), but can drop below  40 °C ( 40 °F) with severe wind chills.[119] In noncoastal regions, snow can cover the ground for almost six months of the year, while in parts of the north snow can persist year-round. Coastal British Columbia has a temperate climate, with a mild and rainy winter. On the east and west coasts, average high temperatures are generally in the low 20s °C (70s °F), while between the coasts, the average summer high temperature ranges from 25 to 30 °C (77 to 86 °F), with temperatures in some interior locations occasionally exceeding 40 °C (104 °F).[120]\r\n"
			+ "Government and politics\r\n" + "Main articles: Government of Canada and Politics of Canada\r\n"
			+ "Elizabeth II\r\n" + "Monarch\r\n" + "Julie Payette\r\n" + "Governor General\r\n" + "Justin Trudeau\r\n"
			+ "Prime Minister\r\n"
			+ "Canada has a parliamentary system within the context of a constitutional monarchy, the monarchy of Canada being the foundation of the executive, legislative, and judicial branches.[121][122][123] The Canadian monarchy is a separate legal institution from the monarchy of the United Kingdom, though the two offices are held by the same individual.[124] The sovereign is Queen Elizabeth II, who is also monarch of 15 other Commonwealth countries and each of Canada's 10 provinces. As such, the Queen's representative, the Governor General of Canada (at present Julie Payette), carries out most of the federal royal duties in Canada.[125][126]\r\n"
			+ "The direct participation of the royal and viceroyal figures in areas of governance is limited.[123][127][128] In practice, their use of the executive powers is directed by the Cabinet, a committee of ministers of the Crown responsible to the elected House of Commons of Canada and chosen and headed by the Prime Minister of Canada (at present Justin Trudeau),[129] the head of government. The governor general or monarch may, though, in certain crisis situations exercise their power without ministerial advice.[127] To ensure the stability of government, the governor general will usually appoint as prime minister the person who is the current leader of the political party that can obtain the confidence of a plurality in the House of Commons.[130] The Prime Minister's Office (PMO) is thus one of the most powerful institutions in government, initiating most legislation for parliamentary approval and selecting for appointment by the Crown, besides the aforementioned, the governor general, lieutenant governors, senators, federal court judges, and heads of Crown corporations and government agencies.[127] The leader of the party with the second-most seats usually becomes the Leader of Her Majesty's Loyal Opposition and is part of an adversarial parliamentary system intended to keep the government in check.[131]\r\n"
			+ "A building with a central clock tower rising from a block\r\n"
			+ "Parliament Hill in Canada's capital city, Ottawa\r\n"
			+ "Each of the 338 members of parliament in the House of Commons is elected by simple plurality in an electoral district or riding. General elections must be called by the governor general, either on the advice of the prime minister or if the government loses a confidence vote in the House.[132][133] Constitutionally, an election may be held no more than five years after the preceding election, although the Canada Elections Act limits this to four years with a fixed election date in October. The 105 members of the Senate, whose seats are apportioned on a regional basis, serve until age 75.[134] Five parties had representatives elected to the federal parliament in the 2015 election: the Liberal Party of Canada who currently form the government, the Conservative Party of Canada who are the Official Opposition, the New Democratic Party, the Bloc Québécois, and the Green Party of Canada.[135]\r\n"
			+ "Canadian Senate chamber long hall with two opposing banks of seats with historical paintings\r\n"
			+ "The Senate chamber within the Centre Block on Parliament Hill\r\n"
			+ "Canada's federal structure divides government responsibilities between the federal government and the ten provinces. Provincial legislatures are unicameral and operate in parliamentary fashion similar to the House of Commons.[128] Canada's three territories also have legislatures, but these are not sovereign and have fewer constitutional responsibilities than the provinces.[136] The territorial legislatures also differ structurally from their provincial counterparts.[137]\r\n"
			+ "The Bank of Canada is the central bank of the country. In addition, the Minister of Finance and Minister of Industry utilize the Statistics Canada agency for financial planning and economic policy development.[138] The Bank of Canada is the sole authority authorized to issue currency in the form of Canadian bank notes.[139] The bank does not issue Canadian coins; they are issued by the Royal Canadian Mint.[140]\r\n"
			+ "Law\r\n" + "Main article: Law of Canada\r\n"
			+ "The Constitution of Canada is the supreme law of the country, and consists of written text and unwritten conventions.[141] The Constitution Act, 1867 (known as the British North America Act prior to 1982), affirmed governance based on parliamentary precedent and divided powers between the federal and provincial governments.[142] The Statute of Westminster 1931 granted full autonomy and the Constitution Act, 1982, ended all legislative ties to the UK, as well as adding a constitutional amending formula and the Canadian Charter of Rights and Freedoms.[143] The Charter guarantees basic rights and freedoms that usually cannot be over-ridden by any government though a notwithstanding clause allows the federal parliament and provincial legislatures to override certain sections of the Charter for a period of five years.[144]\r\n"
			+ "Two sides of a silver medal: the profile of Queen Victoria and the inscription \"Victoria Regina\" on one side, a man in European garb shaking hands with an Aboriginal with the inscription Indian Treaty No. 187 on the other\r\n"
			+ "The Indian Chiefs Medal, presented to commemorate the Numbered Treaties of 1871 1921\r\n"
			+ "The Indian Act, various treaties and case laws were established to mediate relations between Europeans and native peoples.[145] Most notably, a series of eleven treaties known as the Numbered Treaties were signed between the indigenous and the reigning Monarch of Canada between 1871 and 1921.[146] These treaties are agreements with the Canadian Crown-in-Council, administered by Canadian Aboriginal law, and overseen by the Minister of Indigenous and Northern Development. The role of the treaties and the rights they support were reaffirmed by Section Thirty-five of the Constitution Act, 1982.[145] These rights may include provision of services, such as health care, and exemption from taxation.[147] The legal and policy framework within which Canada and First Nations operate was further formalized in 2005, through the First Nations Federal Crown Political Accord.[145]\r\n"
			+ "Supreme Court of Canada building\r\n"
			+ "The Supreme Court of Canada in Ottawa, west of Parliament Hill\r\n"
			+ "Canada's judiciary plays an important role in interpreting laws and has the power to strike down Acts of Parliament that violate the constitution. The Supreme Court of Canada is the highest court and final arbiter and has been led since December 18, 2017 by Chief Justice Richard Wagner.[148] Its nine members are appointed by the governor general on the advice of the prime minister and minister of justice. All judges at the superior and appellate levels are appointed after consultation with non-governmental legal bodies. The federal Cabinet also appoints justices to superior courts in the provincial and territorial jurisdictions.[149]\r\n"
			+ "Common law prevails everywhere except in Quebec, where civil law predominates. Criminal law is solely a federal responsibility and is uniform throughout Canada.[150] Law enforcement, including criminal courts, is officially a provincial responsibility, conducted by provincial and municipal police forces.[151] However, in most rural areas and some urban areas, policing responsibilities are contracted to the federal Royal Canadian Mounted Police.[152]\r\n"
			+ "Foreign relations and military\r\n"
			+ "Main articles: Foreign relations of Canada and Military history of Canada\r\n"
			+ "Pictured from Left to right: C.S. Ritchie, P.E. Renaud, Elizabeth MacCallum, Lucien Moraud, Escott Reid, W.F. Chipman, Lester Pearson, J.H. King, Louis St. Laurent, Rt. Hon. W.L. Mackenzie King, Gordon Graydon, M.J. Coldwell, Cora Casselman, Jean Desy, Hume Wrong, Louis Rasminsky, L.D. Wilgress, M.A. Pope, R. Chaput\r\n"
			+ "The Canadian Delegation to the United Nations Conference on International Organization, San Francisco, May 1945\r\n"
			+ "Canada is recognized as a middle power for its role in international affairs with a tendency to pursue multilateral solutions.[153] Canada's foreign policy based on international peacekeeping and security is carried out through coalitions and international organizations, and through the work of numerous federal institutions.[154] Canada's peacekeeping role during the 20th century has played a major role in its global image.[155] The strategy of the Canadian government's foreign aid policy reflects an emphasis to meet the Millennium Development Goals, while also providing assistance in response to foreign humanitarian crises.[156]\r\n"
			+ "Canada was a founding member of the United Nations and has membership in the World Trade Organization, the G20 and the Organisation for Economic Co-operation and Development (OECD).[153] Canada is also a member of various other international and regional organizations and forums for economic and cultural affairs.[157] Canada acceded to the International Covenant on Civil and Political Rights in 1976.[158] Canada joined the Organization of American States (OAS) in 1990 and hosted the OAS General Assembly in 2000 and the 3rd Summit of the Americas in 2001.[159] Canada seeks to expand its ties to Pacific Rim economies through membership in the Asia-Pacific Economic Cooperation forum (APEC).[160]\r\n"
			+ "Prime Minister Trudeau and U.S. President Trump meet in Washington, February 2017\r\n"
			+ "Canada and the United States share the world's longest undefended border, co-operate on military campaigns and exercises, and are each other's largest trading partner.[161][162] Canada nevertheless has an independent foreign policy, most notably maintaining full relations with Cuba, and declining to officially participate in the 2003 invasion of Iraq.[163] Canada also maintains historic ties to the United Kingdom and France and to other former British and French colonies through Canada's membership in the Commonwealth of Nations and the Francophonie.[164] Canada is noted for having a positive relationship with the Netherlands, owing, in part, to its contribution to the Dutch liberation during World War II.[77]\r\n"
			+ "Canada's strong attachment to the British Empire and Commonwealth led to major participation in British military efforts in the Second Boer War, World War I and World War II.[165] Since then, Canada has been an advocate for multilateralism, making efforts to resolve global issues in collaboration with other nations.[166][167] During the Cold War, Canada was a major contributor to UN forces in the Korean War and founded the North American Aerospace Defense Command (NORAD) in co-operation with the United States to defend against potential aerial attacks from the Soviet Union.[168]\r\n"
			+ "During the Suez Crisis of 1956, future Prime Minister Lester B. Pearson eased tensions by proposing the inception of the United Nations Peacekeeping Force, for which he was awarded the 1957 Nobel Peace Prize.[169] As this was the first UN peacekeeping mission, Pearson is often credited as the inventor of the concept.[170] Canada has since served in over 50 peacekeeping missions, including every UN peacekeeping effort until 1989,[71] and has since maintained forces in international missions in Rwanda, the former Yugoslavia, and elsewhere; Canada has sometimes faced controversy over its involvement in foreign countries, notably in the 1993 Somalia Affair.[171]\r\n"
			+ "Canadian Grenadier Guards in Kandahar Province standing by road with armoured car\r\n"
			+ "Soldiers from the Canadian Grenadier Guards in Kandahar Province in Afghanistan, pictured, fought with Dutch soldiers against Afghan insurgents.\r\n"
			+ "In 2001, Canada deployed troops to Afghanistan as part of the US stabilization force and the UN-authorized, NATO-led International Security Assistance Force.[172] In February 2007, Canada, Italy, the United Kingdom, Norway, and Russia announced their joint commitment to a $1.5-billion project to help develop vaccines for developing nations, and called on other countries to join them.[173] In August 2007, Canada's territorial claims in the Arctic were challenged after a Russian underwater expedition to the North Pole; Canada has considered that area to be sovereign territory since 1925.[174]\r\n"
			+ "The nation employs a professional, volunteer military force of approximately 79,000 active personnel and 32,250 reserve personnel.[175] The unified Canadian Forces (CF) comprise the Canadian Army, Royal Canadian Navy, and Royal Canadian Air Force. In 2013, Canada's military expenditure totalled approximately C$19 billion, or around 1% of the country's GDP.[176][177] Following the 2016 Defence Policy Review, the Canadian government announced a 70% increase to the country's defence budget over the next decade. The Canadian Forces will acquire 88 fighter planes and 15 naval surface combatants, the latter as part of the National Shipbuilding Procurement Strategy. Canada's total military expenditure is expected to reach C$32.7 billion by 2027.[178]\r\n"
			+ "Provinces and territories\r\n" + "Main article: Provinces and territories of Canada\r\n"
			+ "See also: Canadian federalism\r\n"
			+ "Canada is a federation composed of ten provinces and three territories. In turn, these may be grouped into four main regions: Western Canada, Central Canada, Atlantic Canada, and Northern Canada (Eastern Canada refers to Central Canada and Atlantic Canada together). Provinces have more autonomy than territories, having responsibility for social programs such as health care, education, and welfare.[179] Together, the provinces collect more revenue than the federal government, an almost unique structure among federations in the world. Using its spending powers, the federal government can initiate national policies in provincial areas, such as the Canada Health Act; the provinces can opt out of these, but rarely do so in practice. Equalization payments are made by the federal government to ensure that reasonably uniform standards of services and taxation are kept between the richer and poorer provinces.[180]\r\n"
			+ "Clickable map of Canada exhibiting its ten provinces and three territories, and their capitals\r\n"
			+ "A clickable map of Canada exhibiting its ten provinces and three territories, and their capitals.\r\n"
			+ "About this image\r\n" + "Economy\r\n"
			+ "Main articles: Economy of Canada and Economic history of Canada\r\n"
			+ "Canada is the world's tenth-largest economy as of 2016, with a nominal GDP of approximately US$1.52 trillion.[181] It is a member of the Organisation for Economic Co-operation and Development (OECD) and the Group of Eight (G8), and is one of the world's top ten trading nations, with a highly globalized economy.[182][183] Canada is a mixed economy, ranking above the US and most western European nations on The Heritage Foundation's index of economic freedom,[184] and experiencing a relatively low level of income disparity.[185] The country's average household disposable income per capita is over US$23,900, higher than the OECD average.[186] Furthermore, the Toronto Stock Exchange is the seventh-largest stock exchange in the world by market capitalization, listing over 1,500 companies with a combined market capitalization of over US$2 trillion as of 2015.[187]\r\n"
			+ "Chart of exports of Canada by value with percentages\r\n"
			+ "Tree-map of Canada's goods exports in 2014[188]\r\n"
			+ "In 2014, Canada's exports totalled over C$528 billion, while its imported goods were worth over $524 billion, of which approximately $351 billion originated from the United States, $49 billion from the European Union, and $35 billion from China.[189] The country's 2014 trade surplus totalled C$5.1 billion, compared with a C$46.9 billion surplus in 2008.[190][191]\r\n"
			+ "Since the early 20th century, the growth of Canada's manufacturing, mining, and service sectors has transformed the nation from a largely rural economy to an urbanized, industrial one.[192] Like many other developed countries, the Canadian economy is dominated by the service industry, which employs about three-quarters of the country's workforce.[193] However, Canada is unusual among developed countries in the importance of its primary sector, in which the forestry and petroleum industries are two of the most prominent components.[194]\r\n"
			+ "Canada is one of the few developed nations that are net exporters of energy.[195] Atlantic Canada possesses vast offshore deposits of natural gas, and Alberta also hosts large oil and gas resources. The vastness of the Athabasca oil sands and other assets results in Canada having a 13% share of global oil reserves, comprising the world's third-largest share after Venezuela and Saudi Arabia.[196] Canada is additionally one of the world's largest suppliers of agricultural products; the Canadian Prairies are one of the most important global producers of wheat, canola, and other grains.[197] Canada's Ministry of Natural Resources provides statistics regarding its major exports; the country is a leading exporter of zinc, uranium, gold, nickel, aluminum, steel, iron ore, coking coal and lead.[198] Many towns in northern Canada, where agriculture is difficult, are sustainable because of nearby mines or sources of timber. Canada also has a sizeable manufacturing sector centred in southern Ontario and Quebec, with automobiles and aeronautics representing particularly important industries.[199]\r\n"
			+ "Canada's economic integration with the United States has increased significantly since World War II.[200] The Automotive Products Trade Agreement of 1965 opened Canada's borders to trade in the automobile manufacturing industry.[201] In the 1970s, concerns over energy self-sufficiency and foreign ownership in the manufacturing sectors prompted Prime Minister Pierre Trudeau's Liberal government to enact the National Energy Program (NEP) and the Foreign Investment Review Agency (FIRA).[202] In the 1980s, Prime Minister Brian Mulroney's Progressive Conservatives abolished the NEP and changed the name of FIRA to Investment Canada, to encourage foreign investment.[203] The Canada   United States Free Trade Agreement (FTA) of 1988 eliminated tariffs between the two countries, while the North American Free Trade Agreement (NAFTA) expanded the free-trade zone to include Mexico in 1994.[197]\r\n"
			+ "Science and technology\r\n"
			+ "Main articles: Science and technology in Canada and Telecommunications in Canada\r\n"
			+ "A shuttle in space, with Earth in the background. A mechanical arm labelled \"Canada\" rises from the shuttle.\r\n"
			+ "The Canadarm robotic manipulator in action on Space Shuttle Discovery during the STS-116 mission in 2006\r\n"
			+ "In 2015, Canada spent approximately C$31.6 billion on domestic research and development, of which around $7 billion was provided by the federal and provincial governments.[204] As of 2015, the country has produced thirteen Nobel laureates in physics, chemistry, and medicine,[205][206] and was ranked fourth worldwide for scientific research quality in a major 2012 survey of international scientists.[207] It is furthermore home to the headquarters of a number of global technology firms.[208] Canada has one of the highest levels of Internet access in the world, with over 33 million users, equivalent to around 94 percent of its total 2014 population.[209]\r\n"
			+ "The Canadian Space Agency operates a highly active space program, conducting deep-space, planetary, and aviation research, and developing rockets and satellites.[210] Canada was the third country to design and construct a satellite after the Soviet Union and the United States, with the 1962 Alouette 1 launch.[211] Canada is a participant in the International Space Station (ISS), and is a pioneer in space robotics, having constructed the Canadarm, Canadarm2 and Dextre robotic manipulators for the ISS and NASA's Space Shuttle.[212] Since the 1960s, Canada's aerospace industry has designed and built numerous marques of satellite, including Radarsat-1 and 2, ISIS and MOST.[213] Canada has also produced one of the world's most successful and widely used sounding rockets, the Black Brant; over 1,000 Black Brants have been launched since the rocket's introduction in 1961.[214] In 1984, Marc Garneau became Canada's first male astronaut, followed by Canada's second and first female astronaut Roberta Bondar in 1992.[215]\r\n"
			+ "Demographics\r\n" + "Main article: Demographics of Canada\r\n"
			+ "Two-colour map of Windsor area with towns along the St. Lawrence river\r\n"
			+ "The Quebec City Windsor Corridor is the most densely populated and heavily industrialized region of Canada, spanning 1,200 kilometres (750 miles).[216]\r\n"
			+ "Population pyramid 2016\r\n"
			+ "The Canada 2016 Census enumerated a total population of 35,151,728, an increase of around 5.0 percent over the 2011 figure.[217][218] Between 2011 and May 2016, Canada's population grew by 1.7 million people with immigrants accounting for two-thirds of the increase.[219] Between 1990 and 2008, the population increased by 5.6 million, equivalent to 20.4 percent overall growth.[220] The main drivers of population growth are immigration and, to a lesser extent, natural growth.[221]\r\n"
			+ "Canada has one of the highest per-capita immigration rates in the world,[222] driven mainly by economic policy and, to a lesser extent, family reunification.[223][224] The Canadian public as-well as the major political parties support the current level of immigration.[223][225] In 2014, a total of 260,400 immigrants were admitted to Canada, mainly from Asia.[226] The Canadian government anticipated between 280,000 and 305,000 new permanent residents in the following years,[227][228] a similar number of immigrants as in recent years.[229] New immigrants settle mostly in major urban areas such as Toronto, Montreal and Vancouver.[230] Canada also accepts large numbers of refugees,[231] accounting for over 10 percent of annual global refugee resettlements.[232]\r\n"
			+ "Canada's population density, at 3.7 inhabitants per square kilometre (9.6/sq mi), is among the lowest in the world.[233] Canada spans latitudinally from the 83rd parallel north to the 41st parallel north, and approximately 95% of the population is found south of the 55th parallel north.[234] About four-fifths of the population lives within 150 kilometres (93 mi) of the contiguous United States border.[235] The most densely populated part of the country, accounting for nearly 50 percent, is the Quebec City Windsor Corridor, situated in Southern Quebec and Southern Ontario along the Great Lakes and the Saint Lawrence River.[216][234] An additional 30 percent live along the British Columbia Lower Mainland, and the Calgary Edmonton Corridor in Alberta.[236]\r\n"
			+ "In common with many other developed countries, Canada is experiencing a demographic shift towards an older population, with more retirees and fewer people of working age. In 2006, the average age was 39.5 years;[237] by 2011, it had risen to approximately 39.9 years.[238]\r\n"
			+ "As of 2013, the average life expectancy for Canadians is 81 years.[239] The majority of Canadians (69.9%) live in family households, 26.8% report living alone, and those living with unrelated persons reported at 3.7%.[240] The average size of a household in 2006 was 2.5 people.[240]\r\n"
			+ "Largest census metropolitan areas in Canada by population (2016 Census)\r\n" + "    viewtalkedit\r\n"
			+ "CMA 	Province 	Population 		CMA 	Province 	Population\r\n"
			+ "Toronto 	Ontario 	5,928,040 		London 	Ontario 	494,069\r\n"
			+ "Montreal 	Quebec 	4,098,927 		St. Catharines Niagara 	Ontario 	406,074\r\n"
			+ "Vancouver 	British Columbia 	2,463,431 		Halifax 	Nova Scotia 	403,390\r\n"
			+ "Calgary 	Alberta 	1,392,609 		Oshawa 	Ontario 	379,848\r\n"
			+ "Ottawa Gatineau 	Ontario Quebec 	1,323,783 		Victoria 	British Columbia 	367,770\r\n"
			+ "Edmonton 	Alberta 	1,321,426 		Windsor 	Ontario 	329,144\r\n"
			+ "Quebec 	Quebec 	800,296 		Saskatoon 	Saskatchewan 	295,095\r\n"
			+ "Winnipeg 	Manitoba 	778,489 		Regina 	Saskatchewan 	236,481\r\n"
			+ "Hamilton 	Ontario 	747,545 		Sherbrooke 	Quebec 	212,105\r\n"
			+ "Kitchener Cambridge Waterloo 	Ontario 	523,894 		St. John's 	Newfoundland and Labrador 	205,955\r\n"
			+ "Education\r\n" + "Main article: Education in Canada\r\n"
			+ "According to a 2012 report by the Organisation for Economic Co-operation and Development (OECD), Canada is one of the most educated countries in the world;[241] the country ranks first worldwide in the number of adults having tertiary education, with 51 percent of Canadian adults having attained at least an undergraduate college or university degree.[241] Canada spends about 5.3% of its GDP on education.[242] The country invests heavily in tertiary education (more than 20 000 USD per student).[243] As of 2014, 89 percent of adults aged 25 to 64 have earned the equivalent of a high-school degree, compared to an OECD average of 75 percent.[186]\r\n"
			+ "Since the adoption of section 23 of the Constitution Act, 1982, education in both English and French has been available in most places across Canada.[244] Canadian provinces and territories are responsible for education provision.[245] The mandatory school age ranges between 5 7 to 16 18 years,[246] contributing to an adult literacy rate of 99 percent.[105] In 2002, 43 percent of Canadians aged 25 to 64 possessed a post-secondary education; for those aged 25 to 34, the rate of post-secondary education reached 51 percent.[247] The Programme for International Student Assessment indicates that Canadian students perform well above the OECD average, particularly in mathematics, science, and reading.[248][249]\r\n"
			+ "Ethnicity\r\n" + "Main article: Canadians\r\n"
			+ "Self-reported ethnic origins of Canadians based on geographic region (Census 2016)[2]\r\n"
			+ "  indigenous North American (5.06%)\r\n" + "  Other North American [a] (27.61%)\r\n"
			+ "  Europe (46.74%)\r\n" + "  Caribbean and Central and South America (3.38%)\r\n" + "  Africa (2.54%)\r\n"
			+ "  Asia (14.47%)\r\n" + "  Oceania (0.20%)\r\n"
			+ "According to the 2016 census, the country's largest self-reported ethnic origin is Canadian (accounting for 32% of the population),[b] followed by English (18.3%), Scottish (13.9%), French (13.6%), Irish (13.4%), German (9.6%), Chinese (5.1%), Italian (4.6%), First Nations (4.4%), Indian (4.0%), and Ukrainian (3.9%).[250] There are 600 recognized First Nations governments or bands, encompassing a total of 1,525,565 people.[251] Canada's indigenous population is growing at almost twice the national rate, and four percent of Canada's population claimed an indigenous identity in 2006. Another 22.3 percent of the population belonged to a non-indigenous visible minority.[252] In 2016, the largest visible minority groups were South Asian (5.6%), Chinese (5.1%) and Black (3.5%).[252] Between 2011 and 2016, the visible minority population rose by 18.4 percent.[252] In 1961, less than two percent of Canada's population (about 300,000 people) were members of visible minority groups.[253] Indigenous peoples are not considered a visible minority under the Employment Equity Act,[254] and this is the definition that Statistics Canada also uses.\r\n"
			+ "Religion\r\n" + "Main article: Religion in Canada\r\n"
			+ "Canada is religiously diverse, encompassing a wide range of beliefs and customs. Canada has no official church, and the government is officially committed to religious pluralism.[255] Freedom of religion in Canada is a constitutionally protected right, allowing individuals to assemble and worship without limitation or interference.[256] The practice of religion is now generally considered a private matter throughout society and the state.[257] With Christianity in decline after having once been central and integral to Canadian culture and daily life,[258] Canada has become a post-Christian, secular state.[259][260][261][262] The majority of Canadians consider religion to be unimportant in their daily lives,[263] but still believe in God.[264] According to the 2011 census, 67.3% of Canadians identify as Christian; of these, Roman Catholics make up the largest group, accounting for 38.7% of the population. Much of the remainder is made up of Protestants, who accounted for approximately 27% in a 2011 survey.[265][266] The largest Protestant denomination is the United Church of Canada (accounting for 6.1% of Canadians), followed by Anglicans (5.0%), and Baptists (1.9%).[3] Secularization has been growing since the 1960s.[267][268] In 2011, 23.9% declared no religious affiliation, compared to 16.5% in 2001.[269] The remaining 8.8% are affiliated with non-Christian religions, the largest of which are Islam (3.2%) and Hinduism (1.5%).[3]\r\n"
			+ "Languages\r\n" + "Main article: Languages of Canada\r\n"
			+ "Map of Canada with English speakers and French speakers at a percentage\r\n"
			+ "Approximately 98% of Canadians can speak either or both English and French:[270]\r\n"
			+ "  English   56.9%\r\n" + "  English and French   16.1%\r\n" + "  French   21.3%\r\n"
			+ "  Sparsely populated area ( < 0.4 persons per km2)\r\n"
			+ "A multitude of languages are used by Canadians, with English and French (the official languages) being the mother tongues of approximately 56% and 21% of Canadians, respectively.[271] As of the 2016 Census, just over 7.3 million Canadians listed a non-official language as their mother tongue. Some of the most common non-official first languages include Chinese (1,227,680 first-language speakers), Punjabi (501,680), Spanish (458,850), Tagalog (431,385), Arabic (419,895), German (384,040), and Italian (375,645).[271] Canada's federal government practices official bilingualism, which is applied by the Commissioner of Official Languages in consonance with Section 16 of the Canadian Charter of Rights and Freedoms and the Federal Official Languages Act English and French have equal status in federal courts, parliament, and in all federal institutions. Citizens have the right, where there is sufficient demand, to receive federal government services in either English or French and official-language minorities are guaranteed their own schools in all provinces and territories.[272]\r\n"
			+ "The 1977 Charter of the French Language established French as the official language of Quebec.[273] Although more than 85 percent of French-speaking Canadians live in Quebec, there are substantial Francophone populations in New Brunswick, Alberta, and Manitoba; Ontario has the largest French-speaking population outside Quebec.[274] New Brunswick, the only officially bilingual province, has a French-speaking Acadian minority constituting 33 percent of the population.[275] There are also clusters of Acadians in southwestern Nova Scotia, on Cape Breton Island, and through central and western Prince Edward Island.[276]\r\n"
			+ "Other provinces have no official languages as such, but French is used as a language of instruction, in courts, and for other government services, in addition to English. Manitoba, Ontario, and Quebec allow for both English and French to be spoken in the provincial legislatures, and laws are enacted in both languages. In Ontario, French has some legal status, but is not fully co-official.[277] There are 11 indigenous language groups, composed of more than 65 distinct languages and dialects.[278] Of these, only the Cree, Inuktitut and Ojibway languages have a large enough population of fluent speakers to be considered viable to survive in the long term.[279] Several indigenous languages have official status in the Northwest Territories.[280] Inuktitut is the majority language in Nunavut, and is one of three official languages in the territory.[281]\r\n"
			+ "Additionally, Canada is home to many sign languages, some of which are Indigenous.[282] American Sign Language (ASL) is spoken across the country due to the prevalence of ASL in primary and secondary schools.[283] Due to its historical relation to the francophone culture, Quebec Sign Language (LSQ) is spoken primarily in Quebec, although there are sizeable Francophone communities in New Brunswick, Ontario and Manitoba.[284]\r\n"
			+ "Culture\r\n" + "Main article: Culture of Canada\r\n"
			+ "A political cartoon from 1910 on Canada's early European multicultural identity, depicting the French tricolor, the Union Jack, the maple leaf, and fleurs-de-lis.\r\n"
			+ "Canada's culture draws influences from its broad range of constituent nationalities, and policies that promote a \"just society\" are constitutionally protected.[285][286] Canada has placed emphasis on equality and inclusiveness for all its people.[287] Multiculturalism is often cited as one of Canada's significant accomplishments,[288] and a key distinguishing element of Canadian identity.[289][290] In Quebec, cultural identity is strong, and many commentators speak of a culture of Quebec that is distinct from English Canadian culture.[291] However, as a whole, Canada is, in theory, a cultural mosaic a collection of regional ethnic subcultures.[292]\r\n"
			+ "Canada's approach to governance emphasizing multiculturalism, which is based on selective immigration, social integration, and suppression of far-right politics, has wide public support.[293] Government policies such as publicly funded health care, higher taxation to redistribute wealth, the outlawing of capital punishment, strong efforts to eliminate poverty, strict gun control, and the legalization of same-sex marriage are further social indicators of Canada's political and cultural values.[294][295] Canadians also identify with the country's health care institutions, peacekeeping, the National park system and the Canadian Charter of Rights and Freedoms.[289][296]\r\n"
			+ "Bill Reid's 1980 sculpture Raven and The First Men. Raven crushing men under turtle shell\r\n"
			+ "Bill Reid's 1980 sculpture Raven and The First Men; the raven is a figure common to many of Canada's indigenous mythologies\r\n"
			+ "Historically, Canada has been influenced by British, French, and indigenous cultures and traditions. Through their language, art and music, Indigenous peoples continue to influence the Canadian identity.[297] During the 20th century, Canadians with African, Caribbean and Asian nationalities have added to the Canadian identity and its culture.[298] Canadian humour is an integral part of the Canadian identity and is reflected in its folklore, literature, music, art, and media. The primary characteristics of Canadian humour are irony, parody, and satire.[299] Many Canadian comedians have achieved international success in the American TV and film industries and are amongst the most recognized in the world.[300]\r\n"
			+ "Canada has a well-developed media sector, but its cultural output; particularly in English films, television shows, and magazines, is often overshadowed by imports from the United States.[301] As a result, the preservation of a distinctly Canadian culture is supported by federal government programs, laws, and institutions such as the Canadian Broadcasting Corporation (CBC), the National Film Board of Canada (NFB), and the Canadian Radio-television and Telecommunications Commission (CRTC).[302]\r\n"
			+ "Symbols\r\n" + "Main article: National symbols of Canada\r\n"
			+ "The mother beaver sculpture outside the House of Commons\r\n"
			+ "The mother beaver on the Canadian parliament's Peace Tower[303] The five flowers on the shield each represent an ethnicity: Tudor rose: English; Fleur de lis: French; thistle: Scottish; shamrock: Irish; and leek: Welsh.\r\n"
			+ "Canada's national symbols are influenced by natural, historical, and indigenous sources. The use of the maple leaf as a Canadian symbol dates to the early 18th century. The maple leaf is depicted on Canada's current and previous flags, and on the Arms of Canada.[304] The Arms of Canada are closely modelled after the royal coat of arms of the United Kingdom with French and distinctive Canadian elements replacing or added to those derived from the British version.[305] The Great Seal of Canada is a governmental seal used for purposes of state, being set on letters patent, proclamations and commissions, for representatives of the Queen and for the appointment of cabinet ministers, lieutenant governors, senators, and judges.[306][307] Other prominent symbols include the beaver, Canada goose, and common loon, the Crown, the Royal Canadian Mounted Police,[304] and more recently the totem pole and Inuksuk.[308] Canadian coins feature many of these symbols: the loon on the $1 coin, the Arms of Canada on the 50¢ piece, the beaver on the nickel.[309] The penny, removed from circulation in 2013, featured the maple leaf.[310] The Queen' s image appears on $20 bank notes, and on the obverse of all current Canadian coins.[309]\r\n"
			+ "Literature\r\n" + "Main article: Canadian literature\r\n"
			+ "Canadian literature is often divided into French- and English-language literatures, which are rooted in the literary traditions of France and Britain, respectively.[311] There are four major themes that can be found within historical Canadian literature; nature, frontier life, Canada's position within the world, all three of which tie into the garrison mentality.[312] By the 1990s, Canadian literature was viewed as some of the world's best.[313] Canada's ethnic and cultural diversity are reflected in its literature, with many of its most prominent modern writers focusing on ethnic life.[314] Arguably, the best-known living Canadian writer internationally (especially since the deaths of Robertson Davies and Mordecai Richler) is Margaret Atwood, a prolific novelist, poet, and literary critic.[315] Numerous other Canadian authors have accumulated international literary awards;[316] including Nobel Laureate Alice Munro, who has been called the best living writer of short stories in English;[317] and Booker Prize recipient Michael Ondaatje, who is perhaps best known for the novel The English Patient, which was adapted as a film of the same name that won the Academy Award for Best Picture.[318]\r\n"
			+ "Visual arts\r\n" + "Main article: Canadian art\r\n"
			+ "Oil on canvas painting of a tree dominating its rocky landscape during a sunset\r\n"
			+ "The Jack Pine by Tom Thomson. Oil on canvas, 1916, in the collection of the National Gallery of Canada\r\n"
			+ "Canadian visual art has been dominated by figures such as Tom Thomson   the country's most famous painter   and by the Group of Seven.[319] Thomson's career painting Canadian landscapes spanned a decade up to his death in 1917 at age 39.[320] The Group were painters with a nationalistic and idealistic focus, who first exhibited their distinctive works in May 1920. Though referred to as having seven members, five artists Lawren Harris, A. Y. Jackson, Arthur Lismer, J. E. H. MacDonald, and Frederick Varley were responsible for articulating the Group's ideas. They were joined briefly by Frank Johnston, and by commercial artist Franklin Carmichael. A. J. Casson became part of the Group in 1926.[321] Associated with the Group was another prominent Canadian artist, Emily Carr, known for her landscapes and portrayals of the Indigenous peoples of the Pacific Northwest Coast.[322] Since the 1950s, works of Inuit art have been given as gifts to foreign dignitaries by the Canadian government.[323]\r\n"
			+ "Music\r\n" + "Main article: Music of Canada\r\n"
			+ "The Canadian music industry is the sixth-largest in the world producing internationally renowned composers, musicians and ensembles.[324] Music broadcasting in the country is regulated by the CRTC.[325] The Canadian Academy of Recording Arts and Sciences presents Canada's music industry awards, the Juno Awards, which were first awarded in 1970.[326] The Canadian Music Hall of Fame established in 1976 honours Canadian musicians for their lifetime achievements.[327] Patriotic music in Canada dates back over 200 years as a distinct category from British patriotism, preceding the first legal steps to independence by over 50 years. The earliest, The Bold Canadian, was written in 1812.[328] The national anthem of Canada, \"O Canada\", was originally commissioned by the Lieutenant Governor of Quebec, the Honourable Théodore Robitaille, for the 1880 St. Jean-Baptiste Day ceremony, and was officially adopted in 1980.[329] Calixa Lavallée wrote the music, which was a setting of a patriotic poem composed by the poet and judge Sir Adolphe-Basile Routhier. The text was originally only in French before it was translated into English in 1906.[330]\r\n"
			+ "Sport\r\n" + "Main articles: Sports in Canada and History of Canadian sports\r\n"
			+ "Hockey players and fans celebrating\r\n"
			+ "Canada's ice hockey victory at the 2010 Winter Olympics in Vancouver\r\n"
			+ "The roots of organized sports in Canada date back to the 1770s.[331] Canada's official national sports are ice hockey and lacrosse.[332] Golf, tennis, skiing, badminton, volleyball, cycling, swimming, bowling, rugby union, canoeing, equestrian, squash and the study of martial arts are widely enjoyed at the youth and amateur levels.[333]\r\n"
			+ "Canada shares several major professional sports leagues with the United States.[334] Canadian teams in these leagues include seven franchises in the National Hockey League, as well as three Major League Soccer teams and one team in each of Major League Baseball and the National Basketball Association. Other popular professional sports in Canada include Canadian football, which is played in the Canadian Football League, National Lacrosse League lacrosse, and curling.[335]\r\n"
			+ "Canada has participated in almost every Olympic Games since its Olympic debut in 1900,[336] and has hosted several high-profile international sporting events, including the 1976 Summer Olympics,[337] the 1988 Winter Olympics,[338] the 1994 Basketball World Championship,[339] the 2007 FIFA U-20 World Cup,[340] the 2010 Winter Olympics[341][342] and the 2015 FIFA Women's World Cup.[343] Most recently, Canada staged the 2015 Pan American Games and 2015 Parapan American Games.[344]\r\n"
			+ "See also\r\n" + "    flagCanada portal flagNew France portal iconNorth America portal\r\n"
			+ "    Index of Canada-related articles\r\n" + "    Outline of Canada\r\n"
			+ "    Topics by provinces and territories\r\n" + "    Canada   Wikipedia book\r\n" + "Notes\r\n"
			+ "Includes general responses indicating North American origins (e.g., 'North American') as well as more specific responses indicating North American origins that have not been included elsewhere (e.g., 'Maritimer' or 'Quebecois').\r\n"
			+ "    All citizens of Canada are classified as \"Canadians\" as defined by Canada's nationality laws. However, \"Canadian\" as an ethnic group has since 1996 been added to census questionnaires for possible ancestry. \"Canadian\" was included as an example on the English questionnaire and \"Canadien\" as an example on the French questionnaire. \"The majority of respondents to this selection are from the eastern part of the country that was first settled. Respondents generally are visibly European (Anglophones and Francophones), however no-longer self-identify with their ethnic ancestral origins. This response is attributed to a multitude or generational distance from ancestral lineage.\r\n"
			+ "    Source 1: Jack Jedwab (April 2008). \"Our 'Cense' of Self: the 2006 Census saw 1.6 million 'Canadian'\" (PDF). Association for Canadian Studies. Retrieved March 7, 2011.\r\n"
			+ "    Source 2: Don Kerr (2007). The Changing Face of Canada: Essential Readings in Population. Canadian Scholars' Press. pp. 313 317. ISBN 978-1-55130-322-2.\r\n"
			+ "References\r\n"
			+ "D. Michael Jackson (2013). The Crown and Canadian Federalism. Dundurn. p. 199. ISBN 978-1-4597-0989-8. Archived from the original on April 12, 2016.\r\n";

	static {
		minIndex = 1;
		maxIndex = 50;
		// very short strings (e.g. single-char-strings) at high index aren't worth
		// testing because they are not unique at all
		// and can appear at low index, in which case they are the same as
		// single-char-string at low index.
		// start with 8-char so that the sub-strings are more unique.
		minLen = 8;
		maxLen = 20;

		switch (stringLength) {
		case 1:
			// short sub-string (1-20)
			// use default numbers
			break;
		case 2:
			// medium string length, around 100
			minLen = 80;
			maxLen = 120;
			break;
		case 3:
			// long string, around 1k
			minLen = 800;
			maxLen = 1200;
			break;
		case 4:
			// ultra long string, around 10k
			minLen = 9000;
			maxLen = 11000;
			break;
		default:
			break;
		}

		// the whole string is 63k char long
		switch (indexLevel) {
		case 1:
			// low index
			break;
		case 2:
			// medium index, around 100
			minIndex = 80;
			maxIndex = 120;
			break;
		case 3:
			// high index, around 1k
			minIndex = 800;
			maxIndex = 1200;
			break;
		case 4:
			// ultra high index, around 50k
			minIndex = 48000;
			maxIndex = 51000;
			break;
		default:
			break;
		}

		// fixed random sequence
		Random rand = new Random();
		rand.setSeed(68798243246987L);

		// generate a random master string
		// 1: random string
		// 2. wiki article
		if (useRandomString) {
			int stringLen = 64000;
			StringBuffer sb = new StringBuffer();
			sb.setLength(stringLen);

			for (int i = 0; i < stringLen; ++i) {
				int randInt = rand.nextInt(256);
				sb.setCharAt(i, (char) randInt);
			}

			randomCompressible64KCharString = sb.toString();
		}

		// Pre-generate an array of sub-strings
		for (int i = 0; i < MAX_NUM_STRINGS; i++) {
			String substr = null;

			int randLen = rand.nextInt(maxLen - minLen + 1) + minLen;
			int randIndex = rand.nextInt(maxIndex - minIndex + 1) + minIndex;

			if (useRandomString) {
				substr = randomCompressible64KCharString.substring(randIndex, randIndex + randLen);
			} else {
				substr = wikipediaArticleString.substring(randIndex, randIndex + randLen);
			}

			strings[i] = substr;
			indices[i] = randIndex;
		}
	}

	protected int maxIterationsPerLoop() {
		return MAX_NUM_STRINGS;
	}

	int _escape; // To prevent the optimizer from removing the whole method

	int doIndexOfString() {
		String srcString = wikipediaArticleString;

		if (useRandomString) {
			srcString = randomCompressible64KCharString;
		}

		for (int i = 0; i < MAX_NUM_STRINGS; i++) {
			String sub = strings[i];
			_escape = srcString.indexOf(sub);
			resultIndices[i] = _escape;
		}

		return MAX_NUM_STRINGS;
	}

	protected long doBatch(long numLoops) throws InterruptedException {

		for (long loop = 0; loop < numLoops; loop++) {
			startTimer();
			int total = doIndexOfString();
			pauseTimer();
			_escape = total;
		}
		return numLoops * MAX_NUM_STRINGS;
	}
}