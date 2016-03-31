//
// EvhQueryContactDetailById.h
// generated at 2016-03-31 13:49:14 
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

