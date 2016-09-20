//
// EvhPropBillItemDTO.m
//
#import "EvhPropBillItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPropBillItemDTO
//

@implementation EvhPropBillItemDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPropBillItemDTO* obj = [EvhPropBillItemDTO new];
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
    if(self.billId)
        [jsonObject setObject: self.billId forKey: @"billId"];
    if(self.itemName)
        [jsonObject setObject: self.itemName forKey: @"itemName"];
    if(self.startCount)
        [jsonObject setObject: self.startCount forKey: @"startCount"];
    if(self.endCount)
        [jsonObject setObject: self.endCount forKey: @"endCount"];
    if(self.useCount)
        [jsonObject setObject: self.useCount forKey: @"useCount"];
    if(self.price)
        [jsonObject setObject: self.price forKey: @"price"];
    if(self.totalAmount)
        [jsonObject setObject: self.totalAmount forKey: @"totalAmount"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.creatorUid)
        [jsonObject setObject: self.creatorUid forKey: @"creatorUid"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.billId = [jsonObject objectForKey: @"billId"];
        if(self.billId && [self.billId isEqual:[NSNull null]])
            self.billId = nil;

        self.itemName = [jsonObject objectForKey: @"itemName"];
        if(self.itemName && [self.itemName isEqual:[NSNull null]])
            self.itemName = nil;

        self.startCount = [jsonObject objectForKey: @"startCount"];
        if(self.startCount && [self.startCount isEqual:[NSNull null]])
            self.startCount = nil;

        self.endCount = [jsonObject objectForKey: @"endCount"];
        if(self.endCount && [self.endCount isEqual:[NSNull null]])
            self.endCount = nil;

        self.useCount = [jsonObject objectForKey: @"useCount"];
        if(self.useCount && [self.useCount isEqual:[NSNull null]])
            self.useCount = nil;

        self.price = [jsonObject objectForKey: @"price"];
        if(self.price && [self.price isEqual:[NSNull null]])
            self.price = nil;

        self.totalAmount = [jsonObject objectForKey: @"totalAmount"];
        if(self.totalAmount && [self.totalAmount isEqual:[NSNull null]])
            self.totalAmount = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.creatorUid = [jsonObject objectForKey: @"creatorUid"];
        if(self.creatorUid && [self.creatorUid isEqual:[NSNull null]])
            self.creatorUid = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
