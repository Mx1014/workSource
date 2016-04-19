//
// EvhCommunityUserAddressDTO.m
// generated at 2016-04-19 12:41:54 
//
#import "EvhCommunityUserAddressDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityUserAddressDTO
//

@implementation EvhCommunityUserAddressDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCommunityUserAddressDTO* obj = [EvhCommunityUserAddressDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _orgDtos = [NSMutableArray new];
        _addressDtos = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.userName)
        [jsonObject setObject: self.userName forKey: @"userName"];
    if(self.nikeName)
        [jsonObject setObject: self.nikeName forKey: @"nikeName"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.isAuth)
        [jsonObject setObject: self.isAuth forKey: @"isAuth"];
    if(self.applyTime)
        [jsonObject setObject: self.applyTime forKey: @"applyTime"];
    if(self.phone)
        [jsonObject setObject: self.phone forKey: @"phone"];
    if(self.gender)
        [jsonObject setObject: self.gender forKey: @"gender"];
    if(self.orgDtos) {
        NSMutableArray* jsonArray = [NSMutableArray new];
