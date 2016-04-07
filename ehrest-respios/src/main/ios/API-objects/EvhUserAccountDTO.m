//
// EvhUserAccountDTO.m
// generated at 2016-04-07 15:16:51 
//
#import "EvhUserAccountDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserAccountDTO
//

@implementation EvhUserAccountDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUserAccountDTO* obj = [EvhUserAccountDTO new];
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
    if(self.accountId)
        [jsonObject setObject: self.accountId forKey: @"accountId"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.occupyFlag)
        [jsonObject setObject: self.occupyFlag forKey: @"occupyFlag"];
    if(self.confId)
        [jsonObject setObject: self.confId forKey: @"confId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.accountId = [jsonObject objectForKey: @"accountId"];
        if(self.accountId && [self.accountId isEqual:[NSNull null]])
            self.accountId = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.occupyFlag = [jsonObject objectForKey: @"occupyFlag"];
        if(self.occupyFlag && [self.occupyFlag isEqual:[NSNull null]])
            self.occupyFlag = nil;

        self.confId = [jsonObject objectForKey: @"confId"];
        if(self.confId && [self.confId isEqual:[NSNull null]])
            self.confId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
