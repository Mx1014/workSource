//
// EvhDeleteDoorAccessById.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteDoorAccessById
//
@interface EvhDeleteDoorAccessById
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* doorAccessId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

