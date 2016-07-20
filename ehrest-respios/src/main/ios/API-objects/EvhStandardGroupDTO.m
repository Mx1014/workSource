//
// EvhStandardGroupDTO.m
//
#import "EvhStandardGroupDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhStandardGroupDTO
//

@implementation EvhStandardGroupDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhStandardGroupDTO* obj = [EvhStandardGroupDTO new];
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
    if(self.groupType)
        [jsonObject setObject: self.groupType forKey: @"groupType"];
    if(self.groupId)
        [jsonObject setObject: self.groupId forKey: @"groupId"];
    if(self.groupName)
        [jsonObject setObject: self.groupName forKey: @"groupName"];
    if(self.standardId)
        [jsonObject setObject: self.standardId forKey: @"standardId"];
    if(self.inspectorUid)
        [jsonObject setObject: self.inspectorUid forKey: @"inspectorUid"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.groupType = [jsonObject objectForKey: @"groupType"];
        if(self.groupType && [self.groupType isEqual:[NSNull null]])
            self.groupType = nil;

        self.groupId = [jsonObject objectForKey: @"groupId"];
        if(self.groupId && [self.groupId isEqual:[NSNull null]])
            self.groupId = nil;

        self.groupName = [jsonObject objectForKey: @"groupName"];
        if(self.groupName && [self.groupName isEqual:[NSNull null]])
            self.groupName = nil;

        self.standardId = [jsonObject objectForKey: @"standardId"];
        if(self.standardId && [self.standardId isEqual:[NSNull null]])
            self.standardId = nil;

        self.inspectorUid = [jsonObject objectForKey: @"inspectorUid"];
        if(self.inspectorUid && [self.inspectorUid isEqual:[NSNull null]])
            self.inspectorUid = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
