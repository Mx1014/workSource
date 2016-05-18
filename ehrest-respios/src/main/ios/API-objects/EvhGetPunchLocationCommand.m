//
// EvhGetPunchLocationCommand.m
//
#import "EvhGetPunchLocationCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetPunchLocationCommand
//

@implementation EvhGetPunchLocationCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetPunchLocationCommand* obj = [EvhGetPunchLocationCommand new];
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
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.companyId)
        [jsonObject setObject: self.companyId forKey: @"companyId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.companyId = [jsonObject objectForKey: @"companyId"];
        if(self.companyId && [self.companyId isEqual:[NSNull null]])
            self.companyId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
