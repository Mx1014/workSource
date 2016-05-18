//
// EvhGetFamilyMemberInfoCommand.m
//
#import "EvhGetFamilyMemberInfoCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetFamilyMemberInfoCommand
//

@implementation EvhGetFamilyMemberInfoCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetFamilyMemberInfoCommand* obj = [EvhGetFamilyMemberInfoCommand new];
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
    if(self.uid)
        [jsonObject setObject: self.uid forKey: @"uid"];
    if(self.uuid)
        [jsonObject setObject: self.uuid forKey: @"uuid"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.uid = [jsonObject objectForKey: @"uid"];
        if(self.uid && [self.uid isEqual:[NSNull null]])
            self.uid = nil;

        self.uuid = [jsonObject objectForKey: @"uuid"];
        if(self.uuid && [self.uuid isEqual:[NSNull null]])
            self.uuid = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
