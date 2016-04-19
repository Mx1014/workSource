//
// EvhNavigationActionData.h
// generated at 2016-04-19 13:40:01 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNavigationActionData
//
@interface EvhNavigationActionData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* itemLocation;

@property(nonatomic, copy) NSString* layoutName;

// item type NSString*
@property(nonatomic, strong) NSMutableArray* callPhones;

@property(nonatomic, copy) NSString* title;

@property(nonatomic, copy) NSNumber* appId;

@property(nonatomic, copy) NSString* entityTag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

