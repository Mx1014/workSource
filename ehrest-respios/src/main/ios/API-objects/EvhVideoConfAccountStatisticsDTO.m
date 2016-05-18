//
// EvhVideoConfAccountStatisticsDTO.m
//
#import "EvhVideoConfAccountStatisticsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVideoConfAccountStatisticsDTO
//

@implementation EvhVideoConfAccountStatisticsDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhVideoConfAccountStatisticsDTO* obj = [EvhVideoConfAccountStatisticsDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.confAccounts)
        [jsonObject setObject: self.confAccounts forKey: @"confAccounts"];
    if(self.validConfAccount)
        [jsonObject setObject: self.validConfAccount forKey: @"validConfAccount"];
    if(self.theNewConfAccount)
        [jsonObject setObject: self.theNewConfAccount forKey: @"newConfAccount"];
    if(self.confType)
        [jsonObject setObject: self.confType forKey: @"confType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.confAccounts = [jsonObject objectForKey: @"confAccounts"];
        if(self.confAccounts && [self.confAccounts isEqual:[NSNull null]])
            self.confAccounts = nil;

        self.validConfAccount = [jsonObject objectForKey: @"validConfAccount"];
        if(self.validConfAccount && [self.validConfAccount isEqual:[NSNull null]])
            self.validConfAccount = nil;

        self.theNewConfAccount = [jsonObject objectForKey: @"newConfAccount"];
        if(self.theNewConfAccount && [self.theNewConfAccount isEqual:[NSNull null]])
            self.theNewConfAccount = nil;

        self.confType = [jsonObject objectForKey: @"confType"];
        if(self.confType && [self.confType isEqual:[NSNull null]])
            self.confType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
