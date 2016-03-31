//
// EvhMoreActionData.h
// generated at 2016-03-31 13:49:14 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhMoreActionData
//
@interface EvhMoreActionData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* itemLocation;

@property(nonatomic, copy) NSString* itemGroup;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

