//
// EvhQueryContactDetailById.h
// generated at 2016-04-18 14:48:51 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQueryContactDetailById
//
@interface EvhQueryContactDetailById
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* contactId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

