#include <stdio.h>
#include <string.h>

enum {
	SWPORT_WAN = 0,
	SWPORT_LAN1,
	SWPORT_LAN2,
	SWPORT_LAN3,
	SWPORT_LAN4,
#ifdef RTCONFIG_EXT_RTL8365MB
	SWPORT_LAN5,
#endif
	SWPORT_CPU,
	SWPORT_COUNT,
};

/* Default switch config indexes */
enum {
	SWCFG_DEFAULT = 0,	/* 0 Default, ALWAYS first */
	SWCFG_STB1,		/* 1 STB on LAN1 */
	SWCFG_STB2,		/* 2 STB on LAN2 */
	SWCFG_STB3,		/* 3 STB on LAN3 */
	SWCFG_STB4,		/* 4 STB on LAN4 */
	SWCFG_STB12,		/* 5 STB on LAN1&2 */
	SWCFG_STB34,		/* 6 STB on LAN3&4 */
	SWCFG_BRIDGE,		/* 7 Bridge */
	SWCFG_PSTA,		/* 8 PSta */
	WAN1PORT1,
	WAN1PORT2,
	WAN1PORT3,
	WAN1PORT4,
	SWCFG_COUNT
};

/* Phy port logical mask */
enum {
	SW_WAN = (1U << SWPORT_WAN),
	SW_L1  = (1U << SWPORT_LAN1),
	SW_L2  = (1U << SWPORT_LAN2),
	SW_L3  = (1U << SWPORT_LAN3),
	SW_L4  = (1U << SWPORT_LAN4),
#ifdef RTCONFIG_EXT_RTL8365MB
	SW_L5  = (1U << SWPORT_LAN5),
#endif
	SW_CPU = (1U << SWPORT_CPU),
};

void _switch_gen_config(char *buf, const int ports[SWPORT_COUNT], int swmask, char *cputag, int wan)
{
	struct {
		int port;
		char *tag;
	} res[SWPORT_COUNT];
	int i, n, count, mask = swmask;
	char *ptr;

	if (!cputag)
		mask &= ~SW_CPU;

	if (wan && !cputag) {
            for (n = i = count = 0; i < SWPORT_COUNT && mask; mask >>= 1, i++) {
                if ((mask & 1U) == 0)
                        continue;
                res[n].port = ports[i];
                res[n].tag = (i == SWPORT_CPU) ? cputag : "";
                count++;
                n++;
            }
	}
	else {
	    for (i = count = 0; i < SWPORT_COUNT && mask; mask >>= 1, i++) {
		if ((mask & 1U) == 0)
			continue;
		for (n = count; n > 0 && ports[i] < res[n - 1].port; n--)
			res[n] = res[n - 1];
		res[n].port = ports[i];
		res[n].tag = (i == SWPORT_CPU) ? cputag : "";
		count++;
	    }
	}
	for (i = 0, ptr = buf; ptr && i < count; i++)
		ptr += sprintf(ptr, i ? " %d%s" : "%d%s", res[i].port, res[i].tag);
}

#if 0
void switch_gen_config(char *buf, const int ports[SWPORT_COUNT], int index, int wan, char *cputag)
{
	int mask;

	if (!buf || index < SWCFG_DEFAULT || index >= SWCFG_COUNT)
		return;

	mask = wan ? sw_config[index].wanmask : sw_config[index].lanmask;
	_switch_gen_config(buf, ports, mask, cputag, wan);
}
#endif

int main() {
    int ports[SWPORT_COUNT] = { 4, 0, 1, 2, 3, 5 };
    char lan[128] = {0};
    //vlan1ports正确之应该是 3 2 1 0 5*
    //vlan2ports应该是 4 5u
    //我试下来结果是0 1 2 3 5*啊
    //int lanmask = 0xff;
    int lanmask = SW_WAN|SW_L1|SW_L2|SW_L3|SW_L4;
	_switch_gen_config(lan, ports, lanmask, "*", 0);
    printf("rlt: %s\n", lan);
}
