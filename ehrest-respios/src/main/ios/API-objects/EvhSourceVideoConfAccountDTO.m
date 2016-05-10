//
// EvhSourceVideoConfAccountDTO.m
//
#import "EvhSourceVideoConfAccountDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSourceVideoConfAccountDTO
//

@implementation EvhSourceVideoConfAccountDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSourceVideoConfAccountDTO* obj = [EvhSourceVideoConfAccountDTO new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.sourceAccount)
        [jsonObject setObject: self.sourceAccount forKey: @"sourceAccount"];
    if(self.password)
        [jsonObject setObject: self.password forKey: @"password"];
    if(self.confType)
        [jsonObject setObject: self.confType forKey: @"confType"];
    if(self.validDate)
        [jsonObject setObject: self.validDate forKey: @"validDate"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.occupyFlag)
        [jsonObject setObject: self.occupyFlag forKey: @"occupyFlag"];
    if(self.occupyAccountId)
        [jsonObject setObject: self.occupyAccountId forKey: @"occupyAccountId"];
    if(self.confId)
        [jsonObject setObject: self.confId forKey: @"confId"];
    if(self.occupyIdentifierToken)
        [jsonObject setObject: self.occupyIdentifierToken forKey: @"occupyIdentifierToken"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.sourceAccount = [jsonObject objectForKey: @"sourceAccount"];
        if(self.sourceAccount && [self.sourceAccount isEqual:[NSNull null]])
            self.sourceAccount = nil;

        self.password = [jsonObject objectForKey: @"password"];
        if(self.password && [self.password isEqual:[NSNull null]])
            self.password = nil;

        self.confType = [jsonObject objectForKey: @"confType"];
        if(self.confType && [self.confType isEqual:[NSNull null]])
            self.confType = nil;

        self.validDate = [jsonObject objectForKey: @"validDate"];
        if(self.validDate && [self.validDate isEqual:[NSNull null]])
            self.validDate = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.occupyFlag = [jsonObject objectForKey: @"occupyFlag"];
        if(self.occupyFlag && [self.occupyFlag isEqual:[NSNull null]])
            self.occupyFlag = nil;

        self.occupyAccountId = [jsonObject objectForKey: @"occupyAccountId"];
        if(self.occupyAccountId && [self.occupyAccountId isEqual:[NSNull null]])
            self.occupyAccountId = nil;

        self.confId = [jsonObject objectForKey: @"confId"];
        if(self.confId && [self.confId isEqual:[NSNull null]])
            self.confId = nil;

        self.occupyIdentifierToken = [jsonObject objectForKey: @"occupyIdentifierToken"];
        if(self.occupyIdentifierToken && [self.occupyIdentifierToken isEqual:[NSNull null]])
            self.occupyIdentifierToken = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
