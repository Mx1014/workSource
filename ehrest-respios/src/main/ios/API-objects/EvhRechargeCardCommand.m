//
// EvhRechargeCardCommand.m
//
#import "EvhRechargeCardCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRechargeCardCommand
//

@implementation EvhRechargeCardCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRechargeCardCommand* obj = [EvhRechargeCardCommand new];
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
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.cardId)
        [jsonObject setObject: self.cardId forKey: @"cardId"];
    if(self.amount)
        [jsonObject setObject: self.amount forKey: @"amount"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.cardId = [jsonObject objectForKey: @"cardId"];
        if(self.cardId && [self.cardId isEqual:[NSNull null]])
            self.cardId = nil;

        self.amount = [jsonObject objectForKey: @"amount"];
        if(self.amount && [self.amount isEqual:[NSNull null]])
            self.amount = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
