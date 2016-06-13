//
// EvhSearchUsersWithAddrCommand.m
//
#import "EvhSearchUsersWithAddrCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchUsersWithAddrCommand
//

@implementation EvhSearchUsersWithAddrCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSearchUsersWithAddrCommand* obj = [EvhSearchUsersWithAddrCommand new];
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
    if(self.pageOffset)
        [jsonObject setObject: self.pageOffset forKey: @"pageOffset"];
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
    if(self.nickName)
        [jsonObject setObject: self.nickName forKey: @"nickName"];
    if(self.cellPhone)
        [jsonObject setObject: self.cellPhone forKey: @"cellPhone"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.pageOffset = [jsonObject objectForKey: @"pageOffset"];
        if(self.pageOffset && [self.pageOffset isEqual:[NSNull null]])
            self.pageOffset = nil;

        self.pageSize = [jsonObject objectForKey: @"pageSize"];
        if(self.pageSize && [self.pageSize isEqual:[NSNull null]])
            self.pageSize = nil;

        self.nickName = [jsonObject objectForKey: @"nickName"];
        if(self.nickName && [self.nickName isEqual:[NSNull null]])
            self.nickName = nil;

        self.cellPhone = [jsonObject objectForKey: @"cellPhone"];
        if(self.cellPhone && [self.cellPhone isEqual:[NSNull null]])
            self.cellPhone = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
