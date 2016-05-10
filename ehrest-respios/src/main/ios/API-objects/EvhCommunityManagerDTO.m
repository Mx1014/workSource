//
// EvhCommunityManagerDTO.m
//
#import "EvhCommunityManagerDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityManagerDTO
//

@implementation EvhCommunityManagerDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCommunityManagerDTO* obj = [EvhCommunityManagerDTO new];
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
    if(self.managerId)
        [jsonObject setObject: self.managerId forKey: @"managerId"];
    if(self.managerName)
        [jsonObject setObject: self.managerName forKey: @"managerName"];
    if(self.managerPhone)
        [jsonObject setObject: self.managerPhone forKey: @"managerPhone"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.managerId = [jsonObject objectForKey: @"managerId"];
        if(self.managerId && [self.managerId isEqual:[NSNull null]])
            self.managerId = nil;

        self.managerName = [jsonObject objectForKey: @"managerName"];
        if(self.managerName && [self.managerName isEqual:[NSNull null]])
            self.managerName = nil;

        self.managerPhone = [jsonObject objectForKey: @"managerPhone"];
        if(self.managerPhone && [self.managerPhone isEqual:[NSNull null]])
            self.managerPhone = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
